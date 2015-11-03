package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;


import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.fleet.info.result.Settlement;
import cn.xgame.a.player.fleet.info.status.CombatStatus;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

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
		try {
			fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.isEmpty() || !fleet.isCombat() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			CombatStatus status = (CombatStatus) fleet.getStatus();
			// 如果时间还没完 那么继续播放
			if( !status.isComplete() )
				throw new Exception( ErrorCode.SUCCEED.name() );
			
			// 将结果设置一下就行了
			status.setResult( new Settlement() );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 1024 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( fid );
			fleet.getStatus().buildTransformStream(buffer);
		}
		sendPackage( player.getCtx(), buffer );
	}
	
}
