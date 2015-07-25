package cn.xgame.a.player.ectype;

import java.util.HashMap;
import java.util.Map;



/**
 * 偶发副本类型
 * @author deng		
 * @date 2015-7-21 下午2:06:22
 */
public enum EctypeType {

	/**
	 * 常规
	 */
	GENERAL( 1 ),
	
	/**
	 * 登录计时
	 */
	LOGINTIME( 2 ),
	
	/**
	 * 服务器计时
	 */
	SERVERTIME( 3 );
	
	
	private final byte			number;
	EctypeType( int value ) {
		if( value >= Byte.MAX_VALUE || value < 0 ){
			throw new IllegalArgumentException( "不符合规范：" + value );
		}
		this.number 		= (byte) value;
	}
	
	private static final Map<Byte, EctypeType> numToEnum = new HashMap<Byte, EctypeType>();
	static{
		for( EctypeType a : values() ){
			EctypeType p = numToEnum.put( a.number, a );
			if( p != null ){
				throw new RuntimeException( a.number + "重复了" );
			}
		}
	}
	
	public byte toNumber() {
		return number;
	}
	public static EctypeType fromNumber( int n ){
		return numToEnum.get( (byte)n );
	}
}
