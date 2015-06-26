package cn.xgame.a.prop;

import cn.xgame.a.prop.captain.Captains;
import cn.xgame.a.prop.cequip.CEquip;
import cn.xgame.a.prop.sequip.SEquip;
import cn.xgame.a.prop.ship.Ships;
import cn.xgame.a.prop.stuff.Stuff;

/**
 * 道具大类型
 * @author deng		
 * @date 2015-6-18 上午11:14:30
 */
public enum PropType {
	
	/** 材料 */
	STUFF( 1 ) {
		@Override
		public IProp create( int uid, int nid, int count ) {
			return new Stuff( uid, nid, count );
		}
	},
	
	/** 舰长 */
	CAPTAIN( 2 ) {
		@Override
		public IProp create( int uid, int nid, int count) {
			return new Captains( uid, nid, count );
		}
	},
	
	/** 舰船 */
	SHIP( 3 ) {
		@Override
		public IProp create( int uid, int nid, int count) {
			return new Ships( uid, nid, count );
		}
	},
	
	/** 舰长装备 */
	CEQUIP( 4 ) {
		@Override
		public IProp create( int uid, int nid, int count) {
			return new CEquip( uid, nid, count );
		}
	},
	
	/** 舰船装备 */
	SEQUIP( 5 ) {
		@Override
		public IProp create( int uid, int nid, int count) {
			return new SEquip( uid, nid, count );
		}
	};
	
	private final byte	number;
	
	PropType( int n ){
		number = (byte) n;
	}
	
	public byte toNumber(){ return number; }

	public abstract IProp create( int uid, int nid, int count);
	
}
