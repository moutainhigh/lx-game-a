package cn.xgame.a.player.mail.classes;

import io.netty.buffer.ByteBuf;

import cn.xgame.a.IBufferStream;
import cn.xgame.net.netty.Netty.RW;

/**
 * 邮件基类
 * @author deng		
 * @date 2015-10-14 下午5:25:41
 */
public abstract class IMail implements IBufferStream{

	private int uid;
	
	// 邮件类型
	private MailType 		type;
	
	// 邮件标题
	private String 			title;
	
	// 邮件内容
	private String 			content;
	
	// 货币
	private int 			money;
	
	// 附件
//	private List<IProp> adjuncts = Lists.newArrayList();
	
	// 发送人UID
	private String 			senderUID;
	
	// 发送人名字
	private String 			senderName;
	
	// 发送时间
	private int 			sendtime;
	
	// 已读 & 已支付
	private boolean 		isRead = false;
	
	/**
	 * 塞入基础数据 - 用于数据库
	 * @param buffer
	 */
	public void putBufferBase( ByteBuf buffer ){
		buffer.writeInt( uid );
		RW.writeString( buffer, title );
		RW.writeString( buffer, content );
		buffer.writeInt( money );
		RW.writeString( buffer, senderUID );
		RW.writeString( buffer, senderName );
		buffer.writeInt( sendtime );
		buffer.writeByte( isRead ? 1 : 0 );
	}
	
	/**
	 * 包装基础数据 - 用于数据库
	 * @param buffer
	 */
	public void wrapBufferBase( ByteBuf buffer ){
		uid 		= buffer.readInt();
		title 		= RW.readString(buffer);
		content 	= RW.readString(buffer);
		money		= buffer.readInt();
		senderUID	= RW.readString(buffer);
		senderName	= RW.readString(buffer);
		sendtime	= buffer.readInt();
		isRead		= buffer.readByte() == 1;
	}
	
	/**
	 * 塞入标题
	 * @param buffer
	 */
	public void putBufferTitle( ByteBuf buffer ){
		buffer.writeInt( uid );
		buffer.writeByte( type.ordinal() );
		RW.writeString( buffer, title );
		RW.writeString( buffer, senderUID );
		RW.writeString( buffer, senderName );
		buffer.writeInt( sendtime );
		buffer.writeByte( isRead ? 1 : 0 );
	}
	
	/**
	 * 塞入内容
	 * @param buffer
	 */
	public void putBufferContent( ByteBuf buffer ) {
		RW.writeString( buffer, content );
		buffer.writeInt( money );
	}
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public MailType getType() {
		return type;
	}
	public void setType(MailType type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getSendtime() {
		return sendtime;
	}
	public void setSendtime(int sendtime) {
		this.sendtime = sendtime;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public String getSenderUID() {
		return senderUID;
	}
	public void setSenderUID(String senderUID) {
		this.senderUID = senderUID;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}


}
