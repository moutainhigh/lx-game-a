package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 邀请通知
 * @author deng		
 * @date 2015-8-2 下午8:22:04
 */
public class Update_3020 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	/**
	 * 通知
	 * @param to
	 * @param player
	 * @param suid
	 */
	public void run( Player to, Player player, int suid ) {
		
		try {
			ByteBuf response = buildEmptyPackage( to.getCtx(), 30 );
			
			RW.writeString( response, player.getUID() );
			RW.writeString( response, player.getNickname() );
			response.writeInt( suid );
			
			sendPackage( to.getCtx(), response );
			
		} catch (Exception e) {
			Logs.error( "Update_3020 " + e.getMessage() );
		}
	}

}
