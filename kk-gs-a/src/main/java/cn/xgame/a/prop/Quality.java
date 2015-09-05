package cn.xgame.a.prop;

import java.util.HashMap;
import java.util.Map;

/**
 * 道具品质
 * @author deng		
 * @date 2015-9-5 下午5:29:58
 */
public enum Quality {

	DEFAULT(0);
	
	
	private final byte	number;
	private static final Map<Byte, Quality> numToEnum = new HashMap<Byte, Quality>();
	
	Quality( int n ){
		number = (byte) n;
	}

	static{
		for( Quality a : values() ){
			Quality p = numToEnum.put( a.number, a );
			if( p != null ){
				throw new RuntimeException( a.number + "重复了" );
			}
		}
	}
	
	public byte toNumber(){ return number; }
	public static Quality fromNumber( int n ){
		return numToEnum.get( (byte)n );
	}
}
