package cn.xgame.a.player.ship.o.status;

import java.util.HashMap;
import java.util.Map;


/**
 * 舰船状态
 * @author deng		
 * @date 2015-7-31 上午2:08:33
 */
public enum ShipStatus {
	
	/**
	 * 悬停
	 */
	LEVITATION( 1 ),
	
	/**
	 * 航行
	 */
	SAILING( 2 ),
	
	/**
	 * 战斗
	 */
	COMBAT( 3 );
	
	
	private final byte			number;
	
	ShipStatus( int value ) {
		if( value >= Byte.MAX_VALUE || value < 0 ){
			throw new IllegalArgumentException( "不符合规范：" + value );
		}
		this.number 		= (byte) value;
	}
	
	private static final Map<Byte, ShipStatus> numToEnum = new HashMap<Byte, ShipStatus>();
	
	static{
		for( ShipStatus a : values() ){
			
			ShipStatus p = numToEnum.put( a.number, a );
			if( p != null ){
				throw new RuntimeException( "通信包" + a.number + "重复了" );
			}
		}
	}
	
	public byte toNumber() {
		return number;
	}
	public static ShipStatus fromNum( int n ){
		return numToEnum.get( (byte)n );
	}
}
