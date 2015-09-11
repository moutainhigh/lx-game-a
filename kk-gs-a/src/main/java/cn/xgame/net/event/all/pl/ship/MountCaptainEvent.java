package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.dock.capt.CaptainInfo;
import cn.xgame.a.player.dock.ship.ShipInfo;
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
		
		int suid 	= data.readInt();// 舰船UID
		int cuid 	= data.readInt();// 舰长UID
		
		ErrorCode code = null;
		try {
			ShipInfo ship 		= player.getDocks().getShipOfException(suid);
			// 检测是否空闲状态
			player.getDocks().isLeisure( ship );
			
			// 获取舰长
			CaptainInfo ccapt 	= player.getDocks().getCaptainOfException(cuid);
			if( ccapt.getShipUid() != -1 )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 先将舰船已经有个舰长去掉
			player.getDocks().downCaptain( ship );
			
			// 然后将新的舰长 指派到舰船上去
			ship.setCaptainUID( ccapt.getuId() );
			ccapt.setShipUid( ship.getuId() );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 10 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( suid );
			buffer.writeInt( cuid );
		}
		sendPackage( player.getCtx(), buffer );
	}

}
