package cn.xgame.a.prop.info;

import x.javaplus.util.lua.Lua;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.classes.Quality;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Equip_chipPo;
import cn.xgame.config.o.ItemPo;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

/**
 * 舰长装备属性
 * @author deng		
 * @date 2015-6-18 下午1:49:59
 */
public class EquipChipAttr extends IProp{

	private static final int version = 1;
	
	private final Equip_chipPo templet;
	
	// 操控
	private int control;
	
	// 感知
	private int perception;
	
	// 亲和力
	private int affinity;
	
	
	public EquipChipAttr(ItemPo item, int uid, int nid, int count, Quality quality ) {
		super( item, uid, nid, count, quality );
		templet = CsvGen.getEquip_chipPo(nid);
	}
	
	public EquipChipAttr( ByteBuf buf ) {
		super( buf );
		templet = CsvGen.getEquip_chipPo( getNid() );
	}
	
	private EquipChipAttr( EquipChipAttr clone ) {
		super( clone );
		templet 	= clone.templet;
		control 	= clone.control;
		perception 	= clone.perception;
		affinity 	= clone.affinity;
	}

	@Override
	public EquipChipAttr clone() { return new EquipChipAttr(this); }
	
	public Equip_chipPo templet() { return templet; }
	
	@Override
	public byte[] toAttachBytes() {
		ByteBuf buf = Unpooled.buffer(  );
		Lua lua 	= LuaUtil.getDatabaseBufferForm();
		lua.getField( "cequipAttr_ToBytes" ).call( 0, version, buf, this );
		return buf.array();
	}
	
	@Override
	public void wrapAttachBytes(byte[] bytes) {
		if( bytes == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		Lua lua 	= LuaUtil.getDatabaseBufferForm();
		lua.getField( "cequipAttr_WrapBytes" ).call( 0, buf, this );
	}

	@Override
	public void randomAttachAttr() {
		Lua lua = LuaUtil.getGameData();
		lua.getField( "randomAttachAttr" ).call( 0, this );
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( control );
		buffer.writeInt( perception );
		buffer.writeInt( affinity );
	}

	public int getControl() {
		return control;
	}
	public void setControl(int control) {
		this.control = control;
	}
	public int getPerception() {
		return perception;
	}
	public void setPerception(int perception) {
		this.perception = perception;
	}
	public int getAffinity() {
		return affinity;
	}
	public void setAffinity(int affinity) {
		this.affinity = affinity;
	}

	public static void main(String[] args) {
		Lua.setLogClass( Logs.class );
		CsvGen.load();
		ItemPo item = CsvGen.getItemPo( 50001 );
		
		EquipChipAttr attr = new EquipChipAttr( item, 1, item.id, 1, Quality.COLOR01 );
		attr.randomAttachAttr();
		
		System.out.println( "1----- control=" + attr.getControl() + ", perception=" + attr.getPerception() );
		
		byte[] bytes = attr.toAttachBytes();
		
		attr = new EquipChipAttr( item, 1, item.id, 1, Quality.COLOR01 );
		System.out.println( "2----- control=" + attr.getControl() + ", perception=" + attr.getPerception() );
		
		attr.wrapAttachBytes(bytes);
		System.out.println( "3----- control=" + attr.getControl() + ", perception=" + attr.getPerception() );
	}
	
}
