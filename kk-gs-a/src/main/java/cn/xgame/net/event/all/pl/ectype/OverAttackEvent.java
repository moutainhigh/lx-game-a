package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.lua.LuaValue;


import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.ectype.info.ChapterInfo;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.fleet.info.status.CombatStatus;
import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

/**
 * 申请结束副本
 * @author deng
 * @date 2015-7-31 上午7:43:03
 */
public class OverAttackEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	
		byte fid = data.readByte();// 出击舰队ID

		ErrorCode code 		= null;
		FleetInfo fleet 	= null;
		byte iswin			= 0;
		byte starLevel		= 0;
		List<AwardInfo> awards1 = Lists.newArrayList();// 副本掉落
		List<AwardInfo> awards2 = Lists.newArrayList();// 抽奖
		List<IProp> ret		= Lists.newArrayList();
		try {
			fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.isEmpty() || !fleet.isCombat() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			CombatStatus status = (CombatStatus) fleet.getStatus();
			// 如果时间还没完 那么继续播放
			if( !status.isComplete() )
				throw new Exception( ErrorCode.SUCCEED.name() );
			// 获取章节
			ChapterInfo chapter = getChapter( player, fleet.getBerthSnid(), status.getType(), status.getChapterId() );
			// 是否胜利
			iswin			= status.getIsWin();
			// 只有胜利才发放奖励
			if( iswin == 1 ){
				
				StarDepot depot = player.getDepots( fleet.getBerthSnid() );
				// 发放奖励
				awards1.addAll( status.getAwards() );
				for( AwardInfo award : awards1 ){
					ret.addAll( depot.appendProp( award.getId(), award.getCount() ) );
				}
				// 计算评分奖励
				LuaValue[] value = LuaUtil.getEctypeGraded().getField("arithmeticGraded").call( 3, status.getScore() );
				starLevel = value[0].getByte();
				awards2.addAll( chapter.randomSilverAward( value[1].getInt() ) );
				awards2.addAll( chapter.randomGoldenAward( value[2].getInt() ) );
				// 发放评分奖励
				for( AwardInfo award : awards2 ){
					ret.addAll( depot.appendProp( award.getId(), award.getCount() ) );
				}
				
				// 生成下一个难度的副本 
				chapter.generateNextEctype();
				
				// 执行任务
				player.getTasks().execute( ConType.WANCHENGFUBEN, status.getChapterId() );
			}
			
			// 最后 将舰队 设置悬停状态
			fleet.changeStatus( StatusType.HOVER );
			
			Logs.debug( player, "申请副本结束  掉落=" + awards1 + ", 星级=" + starLevel + "-" + awards2 );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 1024 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( fid );
			fleet.getStatus().buildTransformStream(buffer);
			buffer.writeByte( iswin );
			if( iswin == 1 ){
				buffer.writeByte( starLevel );
				// 副本掉落
				buffer.writeByte( awards1.size() );
				for( AwardInfo award : awards1 )
					award.buildTransformStream(buffer);
				// 抽奖个数  第一个默认为银色  第二个为金色
				buffer.writeByte( awards2.size() );
				for( AwardInfo award : awards2 )
					award.buildTransformStream(buffer);
				// 更新道具
				buffer.writeInt( player.getCurrency() );
				buffer.writeByte( ret.size() );
				for( IProp prop : ret ){
					prop.putBaseBuffer(buffer);
					prop.buildTransformStream(buffer);
				}
			}
		}
		sendPackage( player.getCtx(), buffer );
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
}
