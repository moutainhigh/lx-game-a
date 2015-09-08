package cn.xgame.a.prop.cequip;

import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.Quality;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ItemPo;
import cn.xgame.config.o.TreasurePo;
import cn.xgame.utils.LuaUtil;

/**
 * 舰长装备属性
 * @author deng		
 * @date 2015-6-18 下午1:49:59
 */
public class CEquipAttr extends IProp{

	private final TreasurePo templet;
	
	// 操控
	private int control;
	
	// 感知
	private int perception;
	
	// 亲和力
	private int affinity;
	
	
	public CEquipAttr(ItemPo item, int uid, int nid, int count, Quality quality ) {
		super( item, uid, nid, count, quality );
		templet = CsvGen.getTreasurePo(nid);
	}
	
	public CEquipAttr( ByteBuf buf ) {
		super( buf );
		templet = CsvGen.getTreasurePo( getNid() );
	}
	
	private CEquipAttr( CEquipAttr clone ) {
		super( clone );
		templet = clone.templet;
		control = clone.control;
		perception = clone.perception;
		affinity = clone.affinity;
	}

	@Override
	public CEquipAttr clone() { return new CEquipAttr(this); }
	
	public TreasurePo templet() { return templet; }
	
	@Override
	public byte[] toAttachBytes() {
		ByteBuf buf = Unpooled.buffer( 12 );
		buildTransformStream( buf );
		return buf.array();
	}
	@Override
	public void wrapAttachBytes(byte[] bytes) {
		if( bytes == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		control 	= buf.readInt();
		perception 	= buf.readInt();
		affinity 	= buf.readInt();
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( control );
		buffer.writeInt( perception );
		buffer.writeInt( affinity );
	}

	@Override
	public void randomAttachAttr() {
		Lua lua = LuaUtil.getGameData();
		LuaValue[] value = lua.getField( "randomAttachAttr" ).call( 3, templet, type().toNumber(), getQuality().toNumber() );
		control 	= value[0].getInt();
		perception 	= value[1].getInt();
		affinity 	= value[2].getInt();
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

}
