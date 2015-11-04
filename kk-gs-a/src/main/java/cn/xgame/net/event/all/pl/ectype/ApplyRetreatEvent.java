package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.fighter.DamagedInfo;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请撤退
 * @author deng		
 * @date 2015-11-3 下午3:06:01
 */
public class ApplyRetreatEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte fid = data.readByte();// 出击舰队ID

		ErrorCode code 		= null;
		FleetInfo fleet 	= null;
		DamagedInfo dinfo	= null;
		try {
			fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.isEmpty() || !fleet.isCombat() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 先计算战损
			dinfo = fleet.computeDamage( player );
			
			// 设置为悬停
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
			dinfo.buildTransformStream(buffer);
		}
		sendPackage( player.getCtx(), buffer );
	}

}
