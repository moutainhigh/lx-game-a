package cn.xgame.a.prop.cequip;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ItemPo;
import cn.xgame.config.o.TreasurePo;

/**
 * 舰长装备属性
 * @author deng		
 * @date 2015-6-18 下午1:49:59
 */
public class CEquipAttr extends IProp{

	private final TreasurePo templet;
	
		
	public CEquipAttr(ItemPo item, int uid, int nid, int count) {
		super( item, uid, nid, count );
		templet = CsvGen.getTreasurePo(nid);
	}
	
	public CEquipAttr( ByteBuf buf ) {
		super( buf );
		templet = CsvGen.getTreasurePo( getNid() );
	}
	
	private CEquipAttr( CEquipAttr clone ) {
		super( clone );
		templet = clone.templet;
	}

	@Override
	public CEquipAttr clone() { return new CEquipAttr(this); }
	
	public TreasurePo templet() { return templet; }
	
	@Override
	public byte[] toAttachBytes() {
		return null;
	}
	@Override
	public void wrapAttachBytes(byte[] bytes) {
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
	}

}
