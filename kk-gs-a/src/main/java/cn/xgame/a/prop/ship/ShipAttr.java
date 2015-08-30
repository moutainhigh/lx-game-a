package cn.xgame.a.prop.ship;

import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ItemPo;
import cn.xgame.config.o.ShipPo;

/**
 * 舰船属性
 * @author deng		
 * @date 2015-6-18 下午1:48:46
 */
public class ShipAttr extends IProp{

	private final ShipPo templet;
	
	public ShipAttr( ItemPo item, int uid, int nid, int count) {
		super( item, uid, nid, count);
		templet = CsvGen.getShipPo(nid);
	}

	private ShipAttr( ShipAttr clone ){
		super( clone );
		templet = clone.templet;
	}
	
	@Override
	public ShipAttr clone() { return new ShipAttr(this); }
	
	public ShipPo templet(){ return templet; }

	@Override
	public byte[] toAttachBytes() {
		return null;
	}

	@Override
	public void wrapAttachBytes(byte[] bytes) {
		
	}


}
