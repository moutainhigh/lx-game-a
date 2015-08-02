package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.captain.o.CaptainInfo;
import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 指派舰长
 * @author deng		
 * @date 2015-8-3 上午3:36:53
 */
public class MountCaptainEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		int suid = data.readInt();
		int cuid = data.readInt();
		
		ErrorCode code = null;
		try {
			
			ShipInfo ship = player.getDocks().getShip(suid);
			if( ship == null )
				throw new Exception( ErrorCode.SHIP_NOTEXIST.name() );
			CaptainInfo newCaptain = player.getCaptains().getCaptain(cuid);
			if( newCaptain == null )
				throw new Exception( ErrorCode.CAPTAIN_NOTEXIST.name() );

			// 先将舰船已经有个舰长去掉
			CaptainInfo captain = player.getCaptains().getCaptain( ship.getCaptainUID() );
			if( captain != null ) {
				captain.setShipUid( -1 );
				ship.setCaptainUID( -1 );
			}
			
			// 然后将新的舰长 指派到舰船上去
			ship.setCaptainUID( newCaptain.getuId() );
			newCaptain.setShipUid( ship.getuId() );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 2 );
		buffer.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), buffer );
	}

}
