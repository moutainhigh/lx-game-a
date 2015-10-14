package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;


import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.fleet.o.FleetInfo;
import cn.xgame.a.player.fleet.other.StatusType;
import cn.xgame.a.player.fleet.status.CombatStatus;
import cn.xgame.a.player.fleet.status.HoverStatus;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
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
		CombatStatus status = null;
		List<IProp> ret		= Lists.newArrayList();
		byte starLevel		= 0;
		List<IProp> ret1	= Lists.newArrayList();
		List<AwardInfo> awards1 = Lists.newArrayList();
		try {
			Logs.debug( player, "申请结束副本 , 舰队=" + fid );

			FleetInfo fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.getShips().isEmpty() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 判断是不是战斗状态
			if( fleet.getStatus().type() != StatusType.COMBAT )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			else {
				status = (CombatStatus) fleet.getStatus();
				if( !fleet.getStatus().isComplete() )
					throw new Exception( ErrorCode.COMBATTIME_NOTOVER.name() );
			}
			
			// 完了后设置悬停状态
			fleet.setStatus( new HoverStatus() );
			
			StarDepot depot = player.getDepots(fleet.getBerthSnid());
			// 发放奖励
			List<AwardInfo> awards = status.getAwards();
			for( AwardInfo award : awards ){
				ret.addAll( depot.appendProp( award.getId(), award.getCount() ) );
			}
			// 计算评分奖励
			Lua lua 			= LuaUtil.getEctypeGraded();
			LuaValue[] value 	= lua.getField("arithmeticGraded").call( 2, status.getScore(), CsvGen.getEctypePo( status.getEctypeId() ) );
			starLevel 			= value[0].getByte();
			String content 		= value[1].getString();
			// 发送评分奖励
			if( !content.isEmpty() ){
				String[] str = content.split("\\|");
				for( String x : str ){
					String[] a 	= x.split( ";" );
					int id 		= Integer.parseInt( a[0] );
					int count 	= Integer.parseInt( a[1] );
					awards1.add( new AwardInfo(id, count) );
					ret1.addAll( depot.appendProp( id, count ) );
				}
			}
			
			Logs.debug( player, "副本掉落：" + awards + ", 星级：" + starLevel + "-" + ret1 );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		response.writeByte( fid );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeByte( status.getIsWin() );
			if( status.getIsWin() == 1 ){
				// 奖励个数 用于显示
				List<AwardInfo> awards = status.getAwards();
				response.writeByte( awards.size() );
				for( AwardInfo award : awards )
					award.buildTransformStream(response);
				// 奖励个数 用于更新
				response.writeByte( ret.size() );
				for( IProp prop : ret ){
					prop.putBaseBuffer(response);
					prop.buildTransformStream(response);
				}
				// 下面是发送关系评分的
				response.writeByte( starLevel );
				response.writeByte( awards1.size() );
				for( AwardInfo award : awards1 )
					award.buildTransformStream(response);
				response.writeByte( ret1.size() );
				for( IProp prop : ret1 ){
					prop.putBaseBuffer(response);
					prop.buildTransformStream(response);
				}
			}
		}
		if( code == ErrorCode.COMBATTIME_NOTOVER ){
			response.writeInt( status.getEndtime()+1 );
		}
		sendPackage( player.getCtx(), response );
		
	}

}
