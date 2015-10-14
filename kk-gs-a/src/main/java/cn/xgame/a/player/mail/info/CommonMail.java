package cn.xgame.a.player.mail.info;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.mail.classes.IMail;

/**
 * 普通邮件
 * @author deng		
 * @date 2015-10-14 下午5:08:32
 */
public class CommonMail extends IMail {

	/**
	 * 创建一封邮件
	 * @param title
	 * @param content
	 * @param money
	 * @param senderUID
	 * @param senderName
	 */
	public CommonMail(String title, String content, int money, String senderUID, String senderName) {
		setTitle(title);
		setContent(content);
		setMoney(money);
		setSenderUID(senderUID);
		setSenderName(senderName);
		setSendtime( (int) (System.currentTimeMillis()/1000) );
		setRead(false);
	}
	public CommonMail() {
	}

	@Override
	public void putBufferTitle(ByteBuf buffer) {
		super.putBufferTitle(buffer);
		buffer.writeInt(0);
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		super.putBufferBase(buf);
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		super.wrapBufferBase(buf);
	}
	

}
