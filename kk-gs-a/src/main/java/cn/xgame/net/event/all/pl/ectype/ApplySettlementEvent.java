package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;


import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.lua.LuaValue;

import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.fighter.DamagedInfo;
import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.ectype.info.ChapterInfo;
import cn.xgame.a.player.fleet.classes.LotteryInfo;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.fleet.info.status.CombatStatus;
import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.LuaUtil;

/**
 * 申请结算面板
 * @author deng		
 * @date 2015-11-3 下午6:34:26
 */
public class ApplySettlementEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		byte fid = data.readByte();// 出击舰队ID

		ErrorCode code 		= null;
		byte iswin			= 0;
		int fighttime		= 0;
		byte star			= 0;
		byte isLottery		= 0;
		FleetInfo fleet 	= null;
		List<AwardInfo> a 	= null;
		List<IProp> ret		= Lists.newArrayList();
		DamagedInfo dinfo	= null;
		try {
			fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.isEmpty() || !fleet.isCombat() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			CombatStatus status = (CombatStatus) fleet.getStatus();
			// 如果时间还没完 那么继续播放
			if( !status.isComplete() )
				throw new Exception( ErrorCode.SUCCEED.name() );
			
			// 先计算战损
			dinfo = fleet.computeDamage( player );
			
			fighttime = status.getResult().getCtime();
			// 是否胜利
			iswin = status.getIsWin();
			if( iswin == 1 ){
			
				StarDepot depot = player.getDepots( fleet.getBerthSnid() );
				
				a = status.getAwards();
				// 发放奖励
				for( AwardInfo award : a ){
					ret.addAll( depot.appendProp( award.getId(), award.getCount() ) );
				}
				
				// 计算评分奖励
				LuaValue[] value = LuaUtil.getEctypeGraded().getField("arithmeticGraded").call( 3, status.getScore() );
				// 星级
				star = value[0].getByte();
				// 是否可以抽奖
				if( value[1].getInt() == 1 ){
					isLottery = 1;
					fleet.getLotterys().clear();
					fleet.getLotterys().add( new LotteryInfo( status.getChapterId(), 1, player.isVip() ? 2 : 1 ) );
					if( value[2].getInt() == 1 ){
						fleet.getLotterys().add( new LotteryInfo( status.getChapterId(), 2, 1 ) );
					}
				}
				
				end( player, fleet, status );
			}
			
			// 最后 将舰队 设置悬停状态
			fleet.changeStatus( StatusType.HOVER );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 1024 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( fid );
			fleet.getStatus().buildTransformStream(buffer);
			buffer.writeInt( fighttime );
			buffer.writeByte( iswin );
			if( iswin == 1 ){
				buffer.writeByte( star );
				// 前端展示道具
				buffer.writeByte( a.size() );
				for( AwardInfo award : a )
					award.buildTransformStream(buffer);
				// 更新道具
				buffer.writeInt( player.getCurrency() );
				buffer.writeByte( ret.size() );
				for( IProp prop : ret ){
					prop.putBaseBuffer(buffer);
					prop.buildTransformStream(buffer);
				}
				buffer.writeByte( isLottery );
			}
			dinfo.buildTransformStream( buffer );
		}
		sendPackage( player.getCtx(), buffer );
	}

	/**
	 * 最后处理
	 * @param player
	 * @param fleet
	 * @param status
	 */
	private void end( Player player, FleetInfo fleet, CombatStatus status ) {
		
		// 如果不是自己的副本 直接返回
		if( !status.getUID().equals( player.getUID() ) )
			return ;
		
		// 如果是常规副本 那么就生成下一个 难度副本
		if( status.getType() == 1 ){
			player.getEctypes().generateNextGeneralEctype(fleet.getBerthSnid(), status.getChapterId());
		}
		// 如果是偶发副本 那么就生成下一个 难度副本
		if( status.getType() == 2 ){
			ChapterInfo chapter = player.getEctypes().getChapter( fleet.getBerthSnid(), status.getChapterId() );
			if( chapter != null )
				chapter.generateNextEctype();
		}
		// 执行任务
		player.getTasks().execute( ConType.WANCHENGFUBEN, status.getChapterId() );
	}

}

