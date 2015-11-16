package cn.xgame.a.prop.classes;

import java.util.HashMap;
import java.util.Map;

import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.info.EquipAuxiliaryAttr;
import cn.xgame.a.prop.info.EquipChipAttr;
import cn.xgame.a.prop.info.CaptainAttr;
import cn.xgame.a.prop.info.OtherAttr;
import cn.xgame.a.prop.info.EquipWeaponAttr;
import cn.xgame.a.prop.info.ShipAttr;
import cn.xgame.a.prop.info.StuffAttr;
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
			return new OtherAttr( item, uid, nid, count, quality );
		}
	},

	/** 舰船 */
	SHIP( 2 ) {
		@Override
		public IProp create( ItemPo item, int uid, int nid, int count, Quality quality ) {
			return new ShipAttr( item, uid, nid, count, quality );
		}
	},
	
	/** 装备 - 舰长装备 and 舰船装备 */
	EQUIP( 3 ) {
		@Override
		public IProp create( ItemPo item, int uid, int nid, int count, Quality quality ) {
			if( item.itemtype == 3 )// 辅助装备
				return new EquipAuxiliaryAttr(item, uid, nid, count, quality);
			if( item.itemtype == 4 )// 芯片装备
				return new EquipChipAttr( item, uid, nid, count, quality );
			return new EquipWeaponAttr( item, uid, nid, count, quality );
		}
	},
	
	/** 舰长 */
	CAPTAIN( 4 ) {
		@Override
		public IProp create( ItemPo item, int uid, int nid, int count, Quality quality ) {
			return new CaptainAttr( item, uid, nid, count, quality );
		}
	},
		
	/** 材料 */
	STUFF( 5 ) {
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
