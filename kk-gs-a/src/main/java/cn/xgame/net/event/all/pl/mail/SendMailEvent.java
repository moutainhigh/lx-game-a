package cn.xgame.net.event.all.pl.mail;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.mail.classes.IMail;
import cn.xgame.a.player.mail.info.CommonMail;
import cn.xgame.a.player.mail.info.TradeMail;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;

/**
 * 发送邮件
 * @author deng		
 * @date 2015-10-14 下午7:15:51
 */
public class SendMailEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte type = data.readByte();// 发送类型( 1.普通发送 2.交易发送 )
		String recipientsUID = RW.readString(data); // 收件人UID
		String title = RW.readString(data); // 标题;
		String content = RW.readString(data); // 内容;
		int money = data.readInt(); // 货币
		
		ErrorCode code = null;
		try {
			
			Player recipients = PlayerManager.o.getPlayer(recipientsUID, player.getGsid());
			if( recipients == null )
				throw new Exception( ErrorCode.PLAYER_NOTEXIST.name() );
			
			// 算出手续费 这里加上了 交易货币
			int factorage = money + 10;
			
			// 扣取手续费
			if( player.changeCurrency(-factorage) == -1 )
				throw new Exception( ErrorCode.CURRENCY_LAZYWEIGHT.name() );
			
			// 创建邮件
			IMail mail = createMail( type, title, content, money, player );
			
			// 放入邮箱
			recipients.getMails().addMail( mail );
			
			// 如果玩家在线 那么就要提醒
			if( recipients.isOnline() ){
				// TODO
			}
			
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

	/**
	 * 创建一个邮件
	 * @param type
	 * @param title
	 * @param content
	 * @param money
	 * @param player 
	 * @param props
	 * @return
	 */
	private IMail createMail( byte type, String title, String content, int money, Player player ) {
		if( type == 1 ){
			return new TradeMail( title, content, money, player.getUID(), player.getNickname() );
		}else{
			return new CommonMail( title, content, money, player.getUID(), player.getNickname() );
		}
	}

}

class Adjunct{
	int uid;
	int count;
}
