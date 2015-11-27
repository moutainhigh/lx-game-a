package cn.xgame.net.event.all.pl.mail;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.mail.info.MailInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
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
		List<IProp> ret = Lists.newArrayList();
		try {
			
			List<MailInfo> mails = player.getMails().getHavePropMail( uid );
			if( mails.isEmpty() )
				throw new Exception( ErrorCode.MAIL_NOTEXIST.name() );
			
			for( MailInfo mail : mails ){
				player.changeCurrency( mail.getMoney(), "邮件提取" );
				mail.setMoney(0);
				// 这里领取附件道具
				StarDepot depots = player.getDepots();
				for( IProp prop : mail.getProps() ){
					ret.addAll( depots.appendProp(prop) );
				}
				// 然后把道具列表清空掉
				mail.clearupProps();
				
				// 最后保存数据库
				mail.updateDB( player.getUID() );
			}
		
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt(uid);
			buffer.writeInt( player.getCurrency() );
			buffer.writeByte( ret.size() );
			for( IProp prop : ret ){
				prop.putBaseBuffer(buffer);
				prop.buildTransformStream(buffer);
			}
		}
		sendPackage( player.getCtx(), buffer );
	}

}
