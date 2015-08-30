package cn.xgame.a.prop.cequip;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.TreasurePo;
import cn.xgame.gen.dto.MysqlGen.PropsDto;

/**
 * 舰长装备属性
 * @author deng		
 * @date 2015-6-18 下午1:49:59
 */
public class CEquipAttr extends IProp{

	private final TreasurePo templet;
	
		
	public CEquipAttr(int uid, int nid, int count) {
		super( uid, nid, count );
		templet = CsvGen.getTreasurePo(nid);
	}
	
	public CEquipAttr( PropsDto o ) {
		super( o );
		templet = CsvGen.getTreasurePo( getNid() );
	}
	public CEquipAttr( ByteBuf buf ) {
		super( buf );
		templet = CsvGen.getTreasurePo( getNid() );
	}

	@Override
	public IProp clone() {
		CEquipAttr ret = new CEquipAttr(getUid(), getNid(), getCount());
		return ret;
	}
	
	public TreasurePo templet() { return templet; }
	
	@Override
	public byte[] toAttachBytes() {
		return null;
	}
	@Override
	public void wrapAttachBytes(byte[] bytes) {
	}

}
