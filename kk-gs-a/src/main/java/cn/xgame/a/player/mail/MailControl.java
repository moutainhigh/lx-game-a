package cn.xgame.a.player.mail;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.mail.classes.IMail;
import cn.xgame.a.player.mail.classes.MailType;
import cn.xgame.a.player.mail.info.CommonMail;
import cn.xgame.a.player.mail.info.TradeMail;
import cn.xgame.a.player.u.Player;

/**
 * 邮件操作中心
 * @author deng		
 * @date 2015-10-14 下午4:55:20
 */
public class MailControl implements IArrayStream{
	
	// 一页最多个数
	private static final int MAX_NUM = 20;
	

	// 邮件唯一ID
	private int MAIL_UID = 0;
	
	// 所有邮件列表
	private List<IMail> mails = Lists.newArrayList();
	
	
	public MailControl(Player player) {
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return ;
		ByteBuf buff = Unpooled.copiedBuffer(data);
		short size = buff.readShort();
		for( int i = 0; i < size; i++ ){
			MailType type = MailType.values()[buff.readByte()];
			IMail mail = (type == MailType.TRADE ? new TradeMail() : new CommonMail());
			mail.setType(type);
			mail.wrapBuffer(buff);
			mails.add( mail );
		}
	}

	@Override
	public byte[] toBytes() {
		if( mails.isEmpty() ) return null;
		ByteBuf buff = Unpooled.buffer();
		buff.writeShort( mails.size() );
		for( IMail mail : mails ){
			buff.writeByte( mail.getType().ordinal() );
			mail.putBuffer(buff);
		}
		return buff.array();
	}

	public int generateUID(){ return ++MAIL_UID; }
	
	/**
	 * 根据唯一ID 获取邮件 
	 * @param uid
	 * @return
	 */
	public IMail getMail( int uid ) {
		for( IMail mail : mails ){
			if( mail.getUid() == uid )
				return mail;
		}
		return null;
	}
	
	/**
	 * 根据页数获取邮件列表
	 * @param page
	 * @return
	 */
	public List<IMail> getMailsByPage( byte page ) {
		List<IMail> ret = Lists.newArrayList();
		for( int i = (page-1)*MAX_NUM; i < MAX_NUM && i < mails.size(); i++ ){
			ret.add( mails.get(i) );
		}
		return ret;
	}

	/**
	 * 添加一个邮件
	 * @param mail
	 */
	public void addMail( IMail mail ) {
		// 在这里设置邮件UID
		mail.setUid( generateUID() );
		
		mails.add( mail );
	}
	
}
