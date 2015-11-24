package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.Util.Random;
import x.javaplus.util.lua.LuaValue;


import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.classes.TeamAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.ectype.info.ChapterInfo;
import cn.xgame.a.player.ectype.info.EctypeInfo;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.fleet.info.purpose.FightEctype;
import cn.xgame.a.player.fleet.info.result.CombatIn;
import cn.xgame.a.player.fleet.info.status.SailStatus;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.net.event.IEvent;
import cn.xgame.system.LXConstants;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;
import cn.xgame.net.netty.Netty.RW;

/**
 * 开始攻击
 * @author deng
 * @date 2015-7-13 下午7:27:11
 */
public class StartAttackEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {

		String UID 	= RW.readString(data);// 副本所属玩家
		int snid 	= data.readInt();// 星球ID
		byte type	= data.readByte(); // 类型
		int cnid 	= data.readInt();// 章节ID
		byte ltype	= data.readByte();// 难度类型
		byte level	= data.readByte();// 难度
		byte fid 	= data.readByte();// 舰队ID
		
		ErrorCode code 	= null;
		FleetInfo fleet	= null;
		try {
			// 获取舰队
			fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.isEmpty() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			if( fleet.isCombat() )
				throw new Exception( ErrorCode.FLEET_BUSY.name() );
			Player their = PlayerManager.o.getPlayerByTeam(UID);
			if( their == null ){
				fleet.setBerthSnid( snid );
				fleet.changeStatus( StatusType.HOVER );
				throw new Exception( ErrorCode.SUCCEED.name() );
			}
			
			// 获取所有舰队信息 - 这里包括组队的信息
			List<FleetInfo> allfleets = getAllFleets( player, fleet );
			
			// 如果是悬停中  而且还没有在副本地点  那么就需要航行
			if( fleet.isHover() && fleet.getBerthSnid() != snid ){ 
				
				setSailStatus( snid, UID, type, cnid, ltype, level, fleet, allfleets );
				
				throw new Exception( ErrorCode.SUCCEED.name() );
			} 
			
			// 如是航行中
			if( fleet.isSail() ){ 
				
				SailStatus ss = (SailStatus) fleet.getStatus();
				// 航行目的不是打副本直接返回错误
				if( ss.getPurpose().type() != 1 )
					throw new Exception( ErrorCode.OTHER_ERROR.name() );
				
				// 还没航行完 让他继续航行
				if( !ss.isComplete() )
					throw new Exception( ErrorCode.SUCCEED.name() );
				
				// 这里  不用考虑航行完了 没在副本地点 因为战斗航行直接是夸航站的 
			}
			
			// 获取副本信息
			ChapterInfo chapter = getChapter( their, snid, type, cnid );
			EctypeInfo ectype = chapter.getEctype(ltype, level);
			if( ectype == null )
				throw new Exception( ErrorCode.ECTYPE_NOTEXIST.name() );
			
			// 获取玩家战斗力
			Fighter fighter = wrapFighter( allfleets );

			// 全军出击  ...
			LuaValue[] ret = LuaUtil.getEctypeCombat().getField("arithmeticFight").
					call( 5, fighter, ectype.fighter( chapter.getQuestions() ), ectype.getFighttime(), ectype.getMaxWinRate() );
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
			
			// >>>>>>>>> 切换战斗状态
			fleet.setBerthSnid( snid );
			CombatIn result = new CombatIn( (int) (System.currentTimeMillis()/1000), chapter.getDepthtime(), combatTime );
			fleet.changeStatus( StatusType.COMBAT, UID, type, cnid, ltype, level, damaged, ammoExpend, iswin, awards, score, result );
			
			Logs.debug( player, "申请攻打副本 星球ID=" + snid + ", 类型=" + type + ", 章节ID=" + cnid + ", 舰队ID=" + fid );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 1024 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( fid );
			buffer.writeInt( fleet.getBerthSnid() );
			fleet.getStatus().buildTransformStream( buffer );
		}
		sendPackage( player.getCtx(), buffer );
	}

	/**
	 * 包装战斗者
	 * @param allfleets
	 * @return
	 */
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

	/**
	 * 获取玩家包括组队的全部信息
	 * @param player
	 * @param fleet
	 * @return
	 */
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

	/**
	 * 获取章节
	 * @param player
	 * @param snid
	 * @param type
	 * @param cnid
	 * @param level 
	 * @param ltype 
	 * @return
	 * @throws Exception 
	 */
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

	/**
	 * 设置航行状态
	 * @param snid
	 * @param uID 
	 * @param type
	 * @param cnid
	 * @param ltype
	 * @param level
	 * @param fleet
	 */
	private void setSailStatus(int snid, String uID, byte type, int cnid, byte ltype, byte level, FleetInfo fleet, List<FleetInfo> allfleet ) {
		int sailtime 	= LuaUtil.getEctypeCombat().getField( "getSailingTime" ).call( 1, fleet.getBerthSnid(), snid, fleet.toShipDatas() )[0].getInt();
		int starttime 	= (int) (System.currentTimeMillis()/1000);
		FightEctype fightEctype = new FightEctype( uID, type, cnid, ltype, level );
		// 这里根据组队算出 最高航行时间
		int wtime		= sailtime;
		for( FleetInfo x : allfleet ){
			int temp	= LuaUtil.getEctypeCombat().getField( "getSailingTime" ).call( 1, x.getBerthSnid(), snid, x.toShipDatas() )[0].getInt();
			if( temp > wtime )
				wtime	= temp;
		}
		fleet.changeStatus( StatusType.SAIL, snid, starttime, sailtime, fightEctype, wtime-sailtime );
	}
	
}

