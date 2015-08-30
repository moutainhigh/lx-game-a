package cn.xgame.a.prop.ship;

import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ShipPo;
import cn.xgame.gen.dto.MysqlGen.PropsDto;

/**
 * 舰船属性
 * @author deng		
 * @date 2015-6-18 下午1:48:46
 */
public class ShipAttr extends IProp{

	private final ShipPo templet;
	
	public ShipAttr(int uid, int nid, int count) {
		super(uid, nid, count);
		templet = CsvGen.getShipPo(nid);
	}

	public ShipAttr( PropsDto o ){
		super(o);
		templet = CsvGen.getShipPo( getNid() );
	}
	
	@Override
	public IProp clone() {
		ShipAttr ret = new ShipAttr( getUid(), getNid(), getCount());
		return ret;
	}
	
	public ShipPo templet(){ return templet; }

	@Override
	public byte[] toAttachBytes() {
		return null;
	}

	@Override
	public void wrapAttachBytes(byte[] bytes) {
		
	}


}
