package cn.xgame.a.prop;

import java.util.HashMap;
import java.util.Map;

/**
 * 道具品质
 * @author deng		
 * @date 2015-9-5 下午5:29:58
 */
public enum Quality {
	
	/** 灰 */
	COLOR00(0),
	
	/** 白 */
	COLOR01(1),
	
	/** 绿 */
	COLOR02(2),
	
	/** 蛋蓝 */
	COLOR03(3),
	
	/** 深蓝 */
	COLOR04(4),
	
	/** 紫 */
	COLOR05(5),

	/** 大红 */
	COLOR06(6),
	
	/** 橙 */
	COLOR07(7),
	
	/** 粉 */
	COLOR08(8),
	
	/** 金黄 */
	COLOR09(9);
	
	
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
