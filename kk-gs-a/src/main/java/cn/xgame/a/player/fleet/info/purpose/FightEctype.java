package cn.xgame.a.player.fleet.info.purpose;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.Util.Random;
import x.javaplus.util.lua.LuaValue;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.classes.TeamAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.dock.capt.CaptainInfo;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.ectype.info.ChapterInfo;
import cn.xgame.a.player.ectype.info.EctypeInfo;
import cn.xgame.a.player.fleet.classes.IPurpose;
import cn.xgame.a.player.fleet.classes.IResult;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.fleet.info.result.CombatIn;
import cn.xgame.a.player.fleet.info.result.Settlement;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.system.LXConstants;
import cn.xgame.utils.LuaUtil;

/**
 * 打副本
 * @author deng		
 * @date 2015-9-14 下午12:27:03
 */
public class FightEctype extends IPurpose{
	
	// 所属副本玩家UID
	private String UID;
		
	// 副本类型 1.常规副本 2.普通限时 3.特殊限时
	private byte etype;
	
	// 章节ID
	private int chapterId;
	
	// 难度类型 1.普通本 2.挂机本
	private byte ltype;
	
	// 难度
	private byte level;
	
	/**
	 * 创建一个 打副本 的目的
	 * @param uID 
	 * @param etype 副本类型 
	 * @param cnid 章节ID
	 * @param ltype 难度类型 1.普通本 2.挂机本
	 * @param level 难度
	 */
	public FightEctype( String uID, byte etype, int cnid, byte ltype, byte level ) {
		super( (byte) 1 );
		this.UID		= uID;
		this.etype 		= etype;
		this.chapterId 	= cnid;
		this.ltype		= ltype;
		this.level 		= level;
	}
	public FightEctype() {
		super( (byte) 1 );
	}

	@Override
	public void putBuffer( ByteBuf buf ) {
		RW.writeString(buf, UID);
		buf.writeByte( etype );
		buf.writeInt( chapterId );
		buf.writeByte( ltype );
		buf.writeByte( level );
	}
	
	@Override
	public void wrapBuffer(ByteBuf buf) {
		this.UID		= RW.readString(buf);
		this.etype 		= buf.readByte();
		this.chapterId 	= buf.readInt();
		this.ltype		= buf.readByte();
		this.level 		= buf.readByte();
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( type() );
		putBuffer( buffer );
	}

	@Override
	public void execut(  int starttime, int continutime, int targetId, FleetInfo fleet, Player player) {
		
		fleet.setBerthSnid( targetId );
		try {
			
			Player their = PlayerManager.o.getPlayerByTeam(UID);
			if( their == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 获取所有舰队信息 - 这里包括组队的信息
			List<FleetInfo> allfleets = getAllFleets( player, fleet );
			
			// 获取副本信息
			ChapterInfo chapter = getChapter( their, targetId, etype, chapterId );
			EctypeInfo ectype = chapter.getEctype(ltype, level);
			if( ectype == null )
				throw new Exception( ErrorCode.ECTYPE_NOTEXIST.name() );
			
			// 获取玩家战斗力
			Fighter fighter = wrapFighter( allfleets );
			
			LuaValue[] ret = LuaUtil.getEctypeCombat().getField("arithmeticFight").
					call( 5, fighter, ectype.fighter(chapter.getQuestions()), ectype.getFighttime(), ectype.getMaxWinRate() );
			int combatTime	= ret[0].getInt();
			int winRate 	= ret[1].getInt();
			int damaged 	= ret[2].getInt();
			int ammoExpend	= ret[3].getInt();
			int score		= ret[4].getInt();
			
			// 算出胜负
			byte iswin 		= (byte) (Random.get( 0, 10000 ) <= winRate ? 1 : 0);
			// 随机出 胜利后的奖励
			List<AwardInfo> awards = Lists.newArrayList();
			if( iswin == 1 ){
				// 金币奖励
				awards.add( new AwardInfo( LXConstants.CURRENCY_NID, ectype.getMoney() ) );
				// 道具奖励
				awards.addAll( chapter.randomAward( allfleets.size(), ectype.getAwardRate() ) );
			}
			// 计算舰队里面的舰船 战损
			SettlementWardamaged( player, fleet.getShips(), damaged, ammoExpend, iswin );
			
			// 如果这个副本时间也过了 那么直接发放奖励
			IResult result = null;
			int curtime = (int) (System.currentTimeMillis()/1000);
			int endtime = starttime+continutime+combatTime+chapter.getDepthtime()*2;
			if( curtime >= endtime ){
				result = new Settlement();
			}else{
				result = new CombatIn( starttime+continutime, chapter.getDepthtime(), combatTime );
			}
			
			// 改变战斗状态
			fleet.changeStatus( StatusType.COMBAT, UID, etype, chapterId, ltype, level, iswin, awards, score, result );
		} catch (Exception e) {
			
			fleet.changeStatus( StatusType.HOVER );
		}
	}
	
	private void SettlementWardamaged(Player player, List<ShipInfo> ships, int damaged, int ammoExpend, byte iswin) {
		for( ShipInfo ship : ships ){
			// 耐久消耗
			ship.addCurrentHp( damaged );
			// 弹药消耗
			if( Random.get( 0, 10000 ) <= ammoExpend )
				ship.toreduceAmmo( -1 );
			// 舰长忠诚度
			CaptainInfo capt = player.getDocks().getCaptain( ship.getCaptainUID() );
			if( capt.changeLoyalty( iswin == 1 ? 1 : -1 ) ){
				// 这里如果没有忠诚度了 就要删除掉
				player.getDocks().destroy( capt );
				ship.setCaptainUID( -1 );
			}
		}
	}
	
	private Fighter wrapFighter( List<FleetInfo> allfleets ) {
		Fighter ret = new Fighter();
		for( FleetInfo fleet : allfleets ){
			Fighter temp = fleet.fighter();
			ret.hp += temp.hp;
			ret.addAtkattr( temp.attacks );
			ret.addDefattr( temp.defends );
		}
		return ret;
	}
	
	private List<FleetInfo> getAllFleets( Player player, FleetInfo fleet ) {
		List<FleetInfo> ret = Lists.newArrayList();
		// 如果有组队
		if( fleet.getAxnId() != -1 ){
			AxnInfo axn = ChatManager.o.axns().getAXNInfo( fleet.getAxnId() );
			List<IAxnCrew> crews = axn.getAxnCrews();
			for( IAxnCrew crew : crews ){
				if( crew.getUid().equals( player.getUID() ) )
					continue;
				TeamAxnCrew x 	= (TeamAxnCrew) crew;
				Player o 		= PlayerManager.o.getPlayerByTeam( x.getUid() );
				FleetInfo f 	= o.getFleets().getFleetInfo( x.getFid() );
				ret.add( f );
			}
		}
		ret.add( fleet );
		return ret;
	}
	private ChapterInfo getChapter(Player player, int snid, byte type, int cnid ) throws Exception {
		
		switch ( type ) {
		case 1:// 常规副本
			IPlanet planet = WorldManager.o.getPlanet( snid );
			return planet.getChapter( cnid );
		case 2:// 偶发副本
			return player.getEctypes().getChapter( snid, cnid );
		}
		
		throw new Exception( ErrorCode.ECTYPE_NOTEXIST.name() );
	}
	
	public byte getEtype() {
		return etype;
	}
	public int getChapterId() {
		return chapterId;
	}
	public byte getLtype() {
		return ltype;
	}
	public void setLtype(byte ltype) {
		this.ltype = ltype;
	}
	public byte getLevel() {
		return level;
	}
	public void setLevel(byte level) {
		this.level = level;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}

}
