package cn.xgame.a.player.ship.o.v;

import java.util.HashMap;
import java.util.Map;

/**
 * 航行目的
 * @author deng		
 * @date 2015-7-31 上午5:13:49
 */
public enum SailPurpose {

	/**
	 * 1.出航
	 */
	STROLL( 1 ),
	
	/**
	 * 2.返航
	 */
	REVERSAL( 2 ),
	
	/**
	 * 3.打副本
	 */
	FIGHTING( 3 );
	
	
	private final byte			number;
	
	SailPurpose( int value ) {
		if( value >= Byte.MAX_VALUE || value < 0 ){
			throw new IllegalArgumentException( "不符合规范：" + value );
		}
		this.number 		= (byte) value;
	}
	
	private static final Map<Byte, SailPurpose> numToEnum = new HashMap<Byte, SailPurpose>();
	
	static{
		for( SailPurpose a : values() ){
			
			SailPurpose p = numToEnum.put( a.number, a );
			if( p != null ){
				throw new RuntimeException( a.number + "重复了" );
			}
		}
	}
	
	public byte toNumber() {
		return number;
	}
	public static SailPurpose fromNum( int n ){
		return numToEnum.get( (byte)n );
	}
}

