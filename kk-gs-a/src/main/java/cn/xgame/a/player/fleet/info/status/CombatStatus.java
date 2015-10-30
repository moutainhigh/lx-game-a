package cn.xgame.a.player.fleet.info.status;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.lua.LuaValue;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.ectype.info.ChapterInfo;
import cn.xgame.a.player.ectype.info.EctypeInfo;
import cn.xgame.a.player.fleet.classes.IStatus;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.LuaUtil;

/**
 * 战斗状态
 * @author deng		
 * @date 2015-9-11 上午12:39:04
 */
public class CombatStatus extends IStatus{
	
	// 所属副本玩家UID
	private String UID;
	
	// 副本类型 1.常规副本 2.普通限时 3.特殊限时
	private byte type;
	
	// 章节ID
	private int chapterId;
	
	// 难度类型 1.普通本 2.挂机本
	private byte ltype;
	
	// 难度
	private byte level;
	
	// 起始时间
	private int starttime;
	
	// 深度时间 
	private int depthtime;
	
	// 战斗时间
	private int fighttime;
	
	// 是否胜利
	private byte isWin;
	
	// 奖励列表
	private List<AwardInfo> awards = Lists.newArrayList();
	
	// 评分
	private int score;
	

	
	public CombatStatus() {
		super( StatusType.COMBAT );
	}
	
	@Override
	public void init( Object[] objects ) {
		int i = 0;
		UID			= (String) objects[i++];
		type 		= (Byte) objects[i++];
		chapterId 	= (Integer) objects[i++];
		ltype		= (Byte) objects[i++];
		level		= (Byte) objects[i++];
		starttime	= (Integer) objects[i++];
		depthtime	= (Integer) objects[i++];
		fighttime	= (Integer) objects[i++];
		isWin		= (Byte) objects[i++];
		List<?>	x	= (List<?>) objects[i++];
		for( Object o : x )
			awards.add( (AwardInfo) o );
		score		= (Integer) objects[i++];
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		RW.writeString(buf, UID);
		buf.writeByte( type );
		buf.writeInt( chapterId );
		buf.writeByte( ltype );
		buf.writeByte( level );
		buf.writeInt( starttime );
		buf.writeInt( depthtime );
		buf.writeInt( fighttime );
		buf.writeByte( isWin );
		buf.writeByte( awards.size() );
		for( AwardInfo award : awards )
			award.buildTransformStream(buf);
		buf.writeInt( score );
	}
	
	@Override
	public void wrapBuffer(ByteBuf buf) {
		this.UID		= RW.readString(buf);
		this.type 		= buf.readByte();
		this.chapterId 	= buf.readInt();
		this.ltype 		= buf.readByte();
		this.level 		= buf.readByte();
		this.starttime 	= buf.readInt();
		this.depthtime 	= buf.readInt();
		this.fighttime 	= buf.readInt();
		this.isWin 		= buf.readByte();
		byte size		= buf.readByte();
		for( int i = 0; i < size; i++ )
			awards.add( new AwardInfo(buf) );
		score 			= buf.readInt();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		super.buildTransformStream(buffer);
		RW.writeString(buffer, UID);
		buffer.writeByte( type );
		buffer.writeInt( chapterId );
		buffer.writeByte( ltype );
		buffer.writeByte( level );
		buffer.writeInt( getAlreadyFighttime() );
		buffer.writeInt( fighttime );
	}
	
	// 获取已经战斗的时间 - 主要用于前端显示
	private int getAlreadyFighttime() {
		return (int)(System.currentTimeMillis()/1000) - starttime;
	}
	
	@Override
	public boolean isComplete() {
		// 起始时间 + (深度时间 x 2) + 战斗时间 = 结束时间
		int i = starttime + (depthtime*2) + fighttime;
		return (int) (System.currentTimeMillis()/1000) >= i;
	}
	
	@Override
	public void update( FleetInfo fleet, Player player ) {
		if( isWin == 1 ){
			
			StarDepot depot = player.getDepots(fleet.getBerthSnid());

			// 发送奖励
			for( AwardInfo award : awards ){
				depot.appendProp( award.getId(), award.getCount() );
			}
			
			// 这里如果不是自己的副本 那么就不给评分奖励
			Player their = PlayerManager.o.getPlayerByTeam(UID);
			if( their != null ){
				try {
					
					// 获取副本信息
					ChapterInfo chapter = getChapter( their, fleet.getBerthSnid(), type, chapterId );
					EctypeInfo ectype = chapter.getEctype(ltype, level);
					if( ectype == null )
						throw new Exception( ErrorCode.ECTYPE_NOTEXIST.name() );
					
					// 计算评分奖励
					LuaValue[] value = LuaUtil.getEctypeGraded().getField("arithmeticGraded").call( 3, score );
					List<AwardInfo> awards2 = Lists.newArrayList();
					awards2.addAll( chapter.randomSilverAward( value[1].getInt() ) );
					awards2.addAll( chapter.randomGoldenAward( value[2].getInt() ) );
					// 发放评分奖励
					for( AwardInfo award : awards2 ){
						depot.appendProp( award.getId(), award.getCount() );
					}
					
					// 生成下一个难度的副本  当然必须是自己的本才行
					if( UID.equals( player.getUID() ) ){
						chapter.generateNextEctype();
						// 执行任务
						player.getTasks().execute( ConType.WANCHENGFUBEN, chapterId );
					}
					
				} catch (Exception e) { }
			}
		}
		
		// 设置悬停
		fleet.changeStatus( StatusType.HOVER );
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
	
	public byte getType() {
		return type;
	}
	public int getChapterId() {
		return chapterId;
	}
	public byte getIsWin() {
		return isWin;
	}
	public List<AwardInfo> getAwards() {
		return awards;
	}
	public int getScore() {
		return score;
	}
	public byte getLtype() {
		return ltype;
	}
	public byte getLevel() {
		return level;
	}
	public int getStarttime() {
		return starttime;
	}
	public int getDepthtime() {
		return depthtime;
	}
	public int getFighttime() {
		return fighttime;
	}
	public String getUID() {
		return UID;
	}

}
