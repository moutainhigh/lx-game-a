package cn.xgame.a.world.planet.home.o;

import java.util.HashMap;
import java.util.Map;



public enum Institution {
	/**
	 * 独裁体制
	 */
	AUTARCHY( 1 ) {
		@Override
		public boolean isSenator( int i, int privilege ) {
			//独裁体质：一个人的话语权>50%，独裁体质形成
			return i == 0;
		}
	},
	
	/**
	 * 元老体制
	 */
	SENATOR( 2 ) {
		@Override
		public boolean isSenator( int i, int privilege ) {
			//b)元老体质：12个话语权最高的人，话语总和权大于50%
			return i <= 11;
		}
	},
	
	/**
	 * 共和体制
	 */
	REPUBLIC( 3 ) {
		@Override
		public boolean isSenator( int i, int privilege ) {
			//共和体质：12个话语权最高的人，话语权总和小于50（共和体质允许发起投票的人数在13~18之间，当前18个之前就超过50%按超过50%的最小人数算，反之取前18人）
			return privilege <= 5000 && i < 18;
		}
	};
	
	
	private final byte			number;
	Institution( int value ) {
		if( value >= Byte.MAX_VALUE || value < 0 ){
			throw new IllegalArgumentException( "不符合规范：" + value );
		}
		this.number 		= (byte) value;
	}
	
	private static final Map<Byte, Institution> numToEnum = new HashMap<Byte, Institution>();
	static{
		for( Institution a : values() ){
			Institution p = numToEnum.put( a.number, a );
			if( p != null ){
				throw new RuntimeException( a.number + "重复了" );
			}
		}
	}
	
	public byte toNumber() {
		return number;
	}
	public static Institution fromNumber( int n ){
		return numToEnum.get( (byte)n );
	}
	
	/**
	 * 是否元老
	 * @param i 排名
	 * @param privilege 当前总话语权
	 * @return
	 */
	public abstract boolean isSenator( int i, int privilege );
}
