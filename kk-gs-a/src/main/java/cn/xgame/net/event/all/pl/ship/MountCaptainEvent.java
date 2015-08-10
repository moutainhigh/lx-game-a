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
		int suid 	= data.readInt();
		int atsuid 	= data.readInt();
		int cuid 	= data.readInt();
		
		ErrorCode code = null;
		try {
			if( suid == atsuid )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			ShipInfo ship 			= player.getDocks().getShipOfException(suid);
			if( !ship.isLevitation() )
				throw new Exception( ErrorCode.SHIP_NOTLEISURE.name() );
			// 如果是从别个船上取
			ShipInfo atship 		= null;
			if( atsuid != -1 ){
				atship 				= player.getDocks().getShipOfException(atsuid);
				// 判断两个船否在一个星球上
				if( ship.getBerthSnid() != atship.getBerthSnid() )
					throw new Exception( ErrorCode.NOT_ATSAMESTAR.name() ) ;
				if( !atship.isLevitation() )
					throw new Exception( ErrorCode.SHIP_NOTLEISURE.name() );
				if( cuid != atship.getCaptainUID() )
					throw new Exception( ErrorCode.CAPTAIN_NOTEXIST.name() );
			}
			CaptainInfo newCaptain 	= player.getCaptains().getCaptainOfException(cuid);

			// 先将舰船已经有个舰长去掉
			CaptainInfo captain = player.getCaptains().getCaptain( ship.getCaptainUID() );
			if( captain != null ) {
				captain.setShipUid( -1 );
				ship.setCaptainUID( -1 );
			}
			// 这里要把别个船上的舰长去掉
			if( atship != null ){
				atship.setCaptainUID( -1 );
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
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( suid );
			buffer.writeInt( cuid );
		}
		sendPackage( player.getCtx(), buffer );
	}

}
