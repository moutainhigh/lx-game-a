package cn.xgame.a.player.mail.classes;

import java.util.HashMap;
import java.util.Map;


/**
 * 邮件类型
 * @author deng		
 * @date 2015-10-14 下午5:22:21
 */
public enum MailType {
	
	/**
	 * 系统邮件
	 */
	SYSTEM(0),
	
	/**
	 * 普通邮件
	 */
	COMMON(1),
	
	/**
	 * 交易邮件
	 */
	TRADE(2) ;
	
	
	private final byte	number;
	private static final Map<Byte, MailType> numToEnum = new HashMap<Byte, MailType>();
	MailType( int n ){
		number = (byte) n;
	}
	static{
		for( MailType a : values() ){
			MailType p = numToEnum.put( a.number, a );
			if( p != null ){
				throw new RuntimeException( a.number + "重复了" );
			}
		}
	}
	public byte toNumber(){ return number; }
	public static MailType fromNumber( int n ){
		return numToEnum.get( (byte)n );
	}
	
}
