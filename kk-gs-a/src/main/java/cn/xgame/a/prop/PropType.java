package cn.xgame.a.prop;

import java.util.HashMap;
import java.util.Map;

import cn.xgame.a.prop.captain.CaptainAttr;
import cn.xgame.a.prop.cequip.CEquipAttr;
import cn.xgame.a.prop.sequip.SEquipAttr;
import cn.xgame.a.prop.ship.ShipAttr;
import cn.xgame.a.prop.stuff.StuffAttr;
import cn.xgame.config.o.ItemPo;

/**
 * 道具大类型
 * @author deng		
 * @date 2015-6-18 上午11:14:30
 */
public enum PropType {
	
	/** 其他 */
	OTHER( 1 ) {
		@Override
		public IProp create( ItemPo item, int uid, int nid, int count, Quality quality ) {
			return null;
		}
	},

	/** 舰船 */
	SHIP( 2 ) {
		@Override
		public IProp create( ItemPo item, int uid, int nid, int count, Quality quality ) {
			return new ShipAttr( item, uid, nid, count, quality );
		}
	},
	
	/** 舰船装备 */
	SEQUIP( 3 ) {
		@Override
		public IProp create( ItemPo item, int uid, int nid, int count, Quality quality ) {
			return new SEquipAttr( item, uid, nid, count, quality );
		}
	},
	
	/** 舰长 */
	CAPTAIN( 4 ) {
		@Override
		public IProp create( ItemPo item, int uid, int nid, int count, Quality quality ) {
			return new CaptainAttr( item, uid, nid, count, quality );
		}
	},
	
	/** 舰长装备 */
	CEQUIP( 5 ) {
		@Override
		public IProp create( ItemPo item, int uid, int nid, int count, Quality quality ) {
			return new CEquipAttr( item, uid, nid, count, quality );
		}
	},
	
	/** 材料 */
	STUFF( 6 ) {
		@Override
		public IProp create( ItemPo item, int uid, int nid, int count, Quality quality ) {
			return new StuffAttr( item, uid, nid, count, quality );
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
	 * @param item 
	 * @param uid
	 * @param nid
	 * @param count
	 * @param quality 
	 * @return
	 */
	public abstract IProp create( ItemPo item, int uid, int nid, int count, Quality quality);
	
}
