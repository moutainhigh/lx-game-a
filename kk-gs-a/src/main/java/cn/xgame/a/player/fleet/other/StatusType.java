package cn.xgame.a.player.fleet.other;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

import cn.xgame.a.player.fleet.status.CombatStatus;
import cn.xgame.a.player.fleet.status.HoverStatus;
import cn.xgame.a.player.fleet.status.LeisureStatus;
import cn.xgame.a.player.fleet.status.SailStatus;

/**
 * 舰队 状态
 * @author deng		
 * @date 2015-9-11 下午12:22:44
 */
public enum StatusType {
	
	LEISURE(0) {
		@Override
		public IStatus create( ByteBuf buf ) {
			return new LeisureStatus( this, buf );
		}

		@Override
		public IStatus create() {
			return new LeisureStatus( this );
		}
	},
	
	HOVER(1) {
		@Override
		public IStatus create(ByteBuf buf) {
			return new HoverStatus( this, buf );
		}

		@Override
		public IStatus create() {
			return new HoverStatus( this );
		}
	},
	
	SAIL(2) {
		@Override
		public IStatus create(ByteBuf buf) {
			return new SailStatus( this, buf );
		}

		@Override
		public IStatus create() {
			return new SailStatus( this );
		}
	},
	
	COMBAT(3) {
		@Override
		public IStatus create(ByteBuf buf) {
			return new CombatStatus( this, buf );
		}

		@Override
		public IStatus create() {
			return new CombatStatus( this );
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
	public abstract IStatus create(ByteBuf buf);
	
	public abstract IStatus create();
}
