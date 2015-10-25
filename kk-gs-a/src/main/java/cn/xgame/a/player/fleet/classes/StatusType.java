package cn.xgame.a.player.fleet.classes;

import java.util.HashMap;
import java.util.Map;

import cn.xgame.a.player.fleet.info.status.CombatStatus;
import cn.xgame.a.player.fleet.info.status.HoverStatus;
import cn.xgame.a.player.fleet.info.status.LeisureStatus;
import cn.xgame.a.player.fleet.info.status.SailStatus;

/**
 * 舰队 状态
 * @author deng		
 * @date 2015-9-11 下午12:22:44
 */
public enum StatusType {
	
	/** 空闲 */
	LEISURE(0) {
		@Override
		public IStatus create() {
			return new LeisureStatus();
		}
	},
	
	/** 悬停 */
	HOVER(1) {
		@Override
		public IStatus create() {
			return new HoverStatus();
		}
	},
	
	/** 航行 */
	SAIL(2) {
		@Override
		public IStatus create() {
			return new SailStatus();
		}
	},
	
	/** 战斗 */
	COMBAT(3) {
		@Override
		public IStatus create() {
			return new CombatStatus();
		}
	};
	
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
	public abstract IStatus create();
}
