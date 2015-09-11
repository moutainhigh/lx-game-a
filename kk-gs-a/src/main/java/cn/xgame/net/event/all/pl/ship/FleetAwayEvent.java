package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.dock.DockControl;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.fleet.o.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 停用 舰队
 * @author deng		
 * @date 2015-9-11 下午4:29:36
 */
public class FleetAwayEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int suid 	= data.readInt(); // 舰船UID
		byte fid	= data.readByte();// 舰队ID
		
		ErrorCode code = null;
		try {
			// 获取舰船
			DockControl docks 	= player.getDocks();
			ShipInfo ship 		= docks.getShipOfException(suid);
			// 获取舰队
			FleetInfo fleet 	= player.getFleets().getFleetInfo( fid );
			if( fleet == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			if( fleet.getShip(suid) == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			fleet.isLeisure();
			
			// 停用  - 直接删除掉
			fleet.remove( ship );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buf = buildEmptyPackage( player.getCtx(), 7 );
		buf.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buf.writeInt( suid );
			buf.writeByte( fid );
		}
		sendPackage( player.getCtx(), buf );
	}

}
