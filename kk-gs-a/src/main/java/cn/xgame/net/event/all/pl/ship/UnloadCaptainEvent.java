package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.dock.DockControl;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 卸下舰长
 * @author deng		
 * @date 2015-8-3 上午3:51:35
 */
public class UnloadCaptainEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int suid 	= data.readInt(); // 舰船UID
		int cuid 	= data.readInt(); // 舰长UID
		
		ErrorCode code 	= null;
		try {
			
			DockControl docks = player.getDocks();
			ShipInfo ship = docks.getShipOfException(suid);
			if( !docks.isLeisure( ship ) )
				throw new Exception( ErrorCode.SHIP_NOTLEISURE.name() );
			
			if( ship.getCaptainUID() != cuid )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
				
			// 直接卸掉
			docks.downCaptain( ship );
			
			ship.updateDB(player);
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 2 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( suid );
			buffer.writeInt( cuid );
		}
		sendPackage( player.getCtx(), buffer );
	}

}
