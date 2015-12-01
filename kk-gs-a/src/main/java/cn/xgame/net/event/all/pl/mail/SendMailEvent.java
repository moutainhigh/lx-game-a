package cn.xgame.net.event.all.pl.mail;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.mail.info.MailInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.system.LXConstants;
import cn.xgame.system.SystemCfg;
import cn.xgame.net.netty.Netty.RW;

/**
 * 发送邮件
 * @author deng		
 * @date 2015-10-14 下午7:15:51
 */
public class SendMailEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte type 				= data.readByte();// 发送类型( 1.普通发送 2.交易发送 )
		String recipientsUID 	= RW.readString(data); // 收件人UID
		String title 			= RW.readString(data); // 标题;
		String content 			= RW.readString(data); // 内容;
		int money 				= data.readInt(); // 货币
//		List<Prop> props 		= Lists.newArrayList(); // 附件
//		byte size				= data.readByte();
//		for (int i = 0; i < size; i++)
//			props.add( new Prop(data) );
		
		
		ErrorCode code = null;
		try {
			// 算出手续费 这里加上了 交易货币
			int needmoney = LXConstants.MAIL_FACTORAGE + money;
			
			// 扣取手续费
			if( player.changeCurrency( -needmoney, "邮件手续费&交易货币" ) == -1 )
				throw new Exception( ErrorCode.CURRENCY_LAZYWEIGHT.name() );
			
			// 创建邮件
			MailInfo mail = new MailInfo( type, title, content, player );
			mail.addProp( LXConstants.CURRENCY_NID, money );
//			StarDepot depots = player.getDepots();
//			for( Prop prop : props ){
//				depots.deductProp(uid, count);
//				mail.addProp( prop.id, prop.count );
//			}
			
			Player recipients = PlayerManager.o.getPlayer(recipientsUID,SystemCfg.ID);
			if( recipients != null ){
				// 放入玩家邮箱
				recipients.getMails().addMail( mail );
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

}

class Prop{
	int uid;
	int count;
	public Prop(ByteBuf data) {
		uid = data.readInt();
		count = data.readInt();
	}
}

