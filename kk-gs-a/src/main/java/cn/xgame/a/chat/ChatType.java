package cn.xgame.a.chat;

import java.util.HashMap;
import java.util.Map;


/**
 * 聊天频道
 * @author deng		
 * @date 2015-7-25 上午10:06:36
 */
public enum ChatType {

	/** 世界频道 */
	WORLD(1),
	
	/** 母星频道 */
	PLANET(2),
	
	/** 私聊频道 */
	PRIVATE(3),
	
	/** 队伍频道 */
	TEAM(4),
	
	/** 频道1 */
	AXN_1( 10 ),
	
	/** 频道2 */
	AXN_2( 11 ),
	
	/** 频道3 */
	AXN_3( 12 );
	
	
	private final byte	number;
	private static final Map<Byte, ChatType> numToEnum = new HashMap<Byte, ChatType>();
	
	ChatType( int n ){
		number = (byte) n;
	}

	static{
		for( ChatType a : values() ){
			ChatType p = numToEnum.put( a.number, a );
			if( p != null ){
				throw new RuntimeException( a.number + "重复了" );
			}
		}
	}
	
	public byte toNumber(){ return number; }
	public static ChatType fromNumber( int n ){
		return numToEnum.get( (byte)n );
	}

}
