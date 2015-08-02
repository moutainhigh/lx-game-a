package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;

/**
 * 创建队伍
 * @author deng		
 * @date 2015-7-28 下午2:11:00
 */
public class CreateTeamEvent extends IEvent {

	@Override
	public void run( Player player, ByteBuf data ) throws IOException {
		
		int suid 	= data.readInt();
		String uid 	= RW.readString(data);
		
		ErrorCode code = null;
		try {
			
			ShipInfo ship = player.getDocks().getShip(suid);
			if( ship == null )
				throw new Exception( ErrorCode.SHIP_NOTEXIST.name() );
			// 判断船是否空闲中
			if( !ship.isLevitation() )
				throw new Exception( ErrorCode.SHIP_NOTLEISURE.name() );
			// 判断舰船是否已经有队伍了
			if( ship.isHaveTeam() )
				throw new Exception( ErrorCode.SHIP_ISHAVETEAM.name() );
			
			// 判断对方是否还有船是空闲中
			
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		
	}

}
