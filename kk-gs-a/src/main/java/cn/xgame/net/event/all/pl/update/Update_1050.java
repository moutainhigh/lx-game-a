package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;
import cn.xgame.net.netty.Netty.RW;

/**
 * 邮件通知
 * @author deng		
 * @date 2015-10-24 下午9:59:20
 */
public class Update_1050 extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	/**
	 * 通知
	 * @param player 发送人
	 * @param recipients 接受人
	 */
	public void run(Player player, Player recipients) {
		try {
			ByteBuf buffer = buildEmptyPackage( recipients.getCtx(), 125 );
			RW.writeString( buffer, player.getNickname() );
			sendPackage( recipients.getCtx(), buffer );
		} catch (Exception e) {
			Logs.error( "Update_1050 ", e );
		}
	}

}
