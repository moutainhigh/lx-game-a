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
	 * @param to 接受人
	 * @param player 发起人
	 * @param fid 发起人参战舰队ID
	 */
	public void run( Player to, Player player, byte fid ) {
		
		try {
			ByteBuf response = buildEmptyPackage( to.getCtx(), 50 );
			
			RW.writeString( response, player.getUID() );
			RW.writeString( response, player.getNickname() );
			response.writeByte( fid );
			
			sendPackage( to.getCtx(), response );
			
		} catch (Exception e) {
			Logs.error( "Update_3020 " + e.getMessage() );
		}
	}

}
