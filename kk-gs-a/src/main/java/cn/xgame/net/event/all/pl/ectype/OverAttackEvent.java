package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;


import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.fleet.o.FleetInfo;
import cn.xgame.a.player.fleet.other.StatusType;
import cn.xgame.a.player.fleet.status.CombatStatus;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;

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
		List<IProp> ret		= null;
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
			
			// 发放奖励
			List<AwardInfo> awards = status.getAwards();
			for( AwardInfo award : awards ){
				StarDepot depot = player.getDepots(fleet.getBerthSnid());
				ret = depot.appendProp( award.getId(), award.getCount() );
			}
			
			Logs.debug( player, "副本掉落：" + awards );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
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
			}
		}
		if( code == ErrorCode.COMBATTIME_NOTOVER ){
			response.writeInt( status.getEndtime()+1 );
		}
		sendPackage( player.getCtx(), response );
		
	}

}
