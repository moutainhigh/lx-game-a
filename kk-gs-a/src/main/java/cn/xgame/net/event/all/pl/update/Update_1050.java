package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.mail.info.MailInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;

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
	 * 你有新的邮件
	 */
	public void run( Player player, MailInfo mail ) {
		
		try {
			
			if( !player.isOnline() )
				return;
			
			ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
			buffer.writeInt(mail.getUid());
			sendPackage( player.getCtx(), buffer );
			
		} catch (Exception e) {
			Logs.error( "Update_1050 ", e );
		}
	}

}
