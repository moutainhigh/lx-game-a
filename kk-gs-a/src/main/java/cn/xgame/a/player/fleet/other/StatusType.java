package cn.xgame.a.player.fleet.other;

import java.util.HashMap;
import java.util.Map;

/**
 * 舰队 状态
 * @author deng		
 * @date 2015-9-11 下午12:22:44
 */
public enum StatusType {
	
	LEISURE(0),
	
	HOVER(1),
	
	SAIL(2),
	
	COMBAT(3);
	
	private final byte	number;
	private static final Map<Byte, StatusType> numToEnum = new HashMap<Byte, StatusType>();
	StatusType( int n ){
		number = (byte) n;
	}
	static{
		for( StatusType a : values() ){
			StatusType p = numToEnum.put( a.number, a );
			if( p != null ){
				throw new RuntimeException( a.number + "重复了" );
			}
		}
	}
	public byte toNumber(){ return number; }
	public static StatusType fromNumber( int n ){
		return numToEnum.get( (byte)n );
	}
}
