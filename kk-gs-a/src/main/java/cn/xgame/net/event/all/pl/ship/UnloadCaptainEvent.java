package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.captain.o.CaptainInfo;
import cn.xgame.a.player.ship.o.ShipInfo;
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
		
		int suid = data.readInt();
		
		ErrorCode code = null;
		int cuid = 0;
		try {
			
			ShipInfo ship = player.getDocks().getShipOfException(suid);
			
			// 直接卸掉
			CaptainInfo captain = player.getCaptains().getCaptain( ship.getCaptainUID() );
			if( captain != null ) {
				cuid = ship.getCaptainUID();
				captain.setShipUid( -1 );
				ship.setCaptainUID( -1 );
			}
			
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
