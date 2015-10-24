package cn.xgame.net.event.all.pl.mail;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.mail.classes.MailType;
import cn.xgame.a.player.mail.info.MailInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 *  提取附件
 * @author deng		
 * @date 2015-10-14 下午8:20:48
 */
public class ExtractAdjunctEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int uid = data.readInt();
		
		ErrorCode code = null;
		try {
			
			MailInfo mail = player.getMails().getMail( uid );
			if( mail == null )
				throw new Exception( ErrorCode.MAIL_NOTEXIST.name() );
			
			// 如果不是交易类型的邮件 那么就可以直接领取货币
			if( mail.getType() != MailType.TRADE ){
				player.changeCurrency( mail.getMoney(), "邮件提取" );
				mail.setMoney(0);
				mail.updateDB( player.getUID() );
			}
			// 这里领取附件道具
			// TODO
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( player.getCurrency() );
		}
		sendPackage( player.getCtx(), buffer );
	}

}
