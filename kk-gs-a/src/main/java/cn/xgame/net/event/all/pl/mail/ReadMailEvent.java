package cn.xgame.net.event.all.pl.mail;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.mail.classes.IMail;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 读取邮件
 * @author deng		
 * @date 2015-10-14 下午8:15:02
 */
public class ReadMailEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int uid = data.readInt();
		
		ErrorCode code = null;
		IMail mail = null;
		try {
			
			mail = player.getMails().getMail( uid );
			if( mail == null )
				throw new Exception( ErrorCode.ACCOUNT_EXIST.name() );
			
			// 设置已读
			mail.setRead(true);
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 2 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			mail.putBufferContent( buffer );
		}
		sendPackage( player.getCtx(), buffer );
	}

}
