package cn.xgame.a.player.mail.info;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.mail.classes.IMail;
import cn.xgame.system.LXConstants;

/**
 * 交易邮件
 * @author deng		
 * @date 2015-10-14 下午5:33:37
 */
public class TradeMail extends IMail{

	// 时效
	private int endtime;

	
	public TradeMail(String title, String content, int money, String senderUID, String senderName ) {
		setTitle(title);
		setContent(content);
		setMoney(money);
		setSenderUID(senderUID);
		setSenderName(senderName);
		setSendtime( (int) (System.currentTimeMillis()/1000) );
		setRead(false);
		setEndtime( (int)(System.currentTimeMillis()/1000) + LXConstants.MAIL_DURATION );
	}
	public TradeMail() {
	}

	@Override
	public void putBufferTitle(ByteBuf buffer) {
		super.putBufferTitle(buffer);
		buffer.writeInt( endtime );
	}

	public int getEndtime() {
		return endtime;
	}
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		super.putBufferBase(buf);
		buf.writeInt(endtime);
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		super.wrapBufferBase(buf);
		endtime = buf.readInt();
	}
	
}
