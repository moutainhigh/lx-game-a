package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;
import cn.xgame.net.netty.Netty.RW;

/**
 * 有人退出队伍 通知
 * @author deng		
 * @date 2015-10-23 下午8:39:47
 */
public class Update_2301 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	public void run( int axnId, String UID, Player accept ) {
		
		try {
			ByteBuf buffer = buildEmptyPackage( accept.getCtx(), 50 );
			buffer.writeInt( axnId );
			RW.writeString( buffer, UID );
			sendPackage( accept.getCtx(), buffer );
		} catch (Exception e) {
			Logs.error( "Update_2301 ", e );
		}
	}

}
