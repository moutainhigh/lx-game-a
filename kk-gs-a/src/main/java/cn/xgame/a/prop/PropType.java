package cn.xgame.a.prop;

import java.util.HashMap;
import java.util.Map;

import cn.xgame.a.prop.captain.Captains;
import cn.xgame.a.prop.cequip.CEquip;
import cn.xgame.a.prop.sequip.SEquip;
import cn.xgame.a.prop.ship.Ships;
import cn.xgame.a.prop.stuff.Stuff;
import cn.xgame.gen.dto.MysqlGen.PropsDto;

/**
 * 道具大类型
 * @author deng		
 * @date 2015-6-18 上午11:14:30
 */
public enum PropType {
	
	/** 其他 */
	OTHER( 1 ) {
		@Override
		public IProp create( int uid, int nid, int count ) {
			return null;
		}
		@Override
		public IProp wrapDB(PropsDto dto) {
			return null;
		}
	},

	/** 舰船 */
	SHIP( 2 ) {
		@Override
		public IProp create( int uid, int nid, int count) {
			return new Ships( uid, nid, count );
		}
		@Override
		public IProp wrapDB( PropsDto dto ) {
			return new Ships( dto );
		}
	},
	
	/** 舰船装备 */
	SEQUIP( 3 ) {
		@Override
		public IProp create( int uid, int nid, int count) {
			return new SEquip( uid, nid, count );
		}
		@Override
		public IProp wrapDB(PropsDto dto) {
			return new SEquip( dto );
		}
	},
	
	/** 舰长 */
	CAPTAIN( 4 ) {
		@Override
		public IProp create( int uid, int nid, int count) {
			return new Captains( uid, nid, count );
		}
		@Override
		public IProp wrapDB(PropsDto dto) {
			return new Captains( dto );
		}
	},
	
	/** 舰长装备 */
	CEQUIP( 5 ) {
		@Override
		public IProp create( int uid, int nid, int count) {
			return new CEquip( uid, nid, count );
		}
		@Override
		public IProp wrapDB(PropsDto dto) {
			return new CEquip( dto );
		}
	},
	
	/** 材料 */
	STUFF( 6 ) {
		@Override
		public IProp create( int uid, int nid, int count ) {
			return new Stuff( uid, nid, count );
		}
		@Override
		public IProp wrapDB(PropsDto dto) {
			return new Stuff( dto );
		}
	};
	
	private final byte	number;
	private static final Map<Byte, PropType> numToEnum = new HashMap<Byte, PropType>();
	
	PropType( int n ){
		number = (byte) n;
	}

	static{
		for( PropType a : values() ){
			PropType p = numToEnum.put( a.number, a );
			if( p != null ){
				throw new RuntimeException( a.number + "重复了" );
			}
		}
	}
	
	public byte toNumber(){ return number; }
	public static PropType fromNumber( int n ){
		return numToEnum.get( (byte)n );
	}

	/**
	 * 创建一个 道具
	 * @param uid
	 * @param nid
	 * @param count
	 * @return
	 */
	public abstract IProp create( int uid, int nid, int count);
	
	/**
	 * 从数据库获取一个 道具
	 * @param dto
	 * @return
	 */
	public abstract IProp wrapDB( PropsDto dto );
	
}
