package cn.xgame.a.prop.info;

import x.javaplus.util.lua.Lua;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.classes.Quality;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ItemPo;
import cn.xgame.config.o.ShipPo;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

/**
 * 舰船属性
 * @author deng		
 * @date 2015-6-18 下午1:48:46
 */
public class ShipAttr extends IProp{

	private static final int version = 1;
	
	private final ShipPo templet;
	
	// 最大血量
	private int maxHp;
	
	// 最大能源
	private int maxEnergy;
	
	// 质量
	private int mass;
	
	// 精密度
	private int accuracy;
	
	//------------------- 下面三个空间不用存数据库 就只是一个厨初始数据
	// 武器最大空间
	private short wroom;
	
	// 辅助最大空间
	private short eroom;
	
	// 货仓最大空间
	private short groom;
	
	public ShipAttr( ItemPo item, int uid, int nid, int count, Quality quality) {
		super( item, uid, nid, count, quality );
		templet 	= CsvGen.getShipPo(nid);
	}

	private ShipAttr( ShipAttr clone ){
		super( clone );
		templet 	= clone.templet;
		maxHp		= clone.maxHp;
		maxEnergy	= clone.maxEnergy;
		mass		= clone.mass;
		accuracy	= clone.accuracy;
		wroom		= clone.wroom;
		eroom		= clone.eroom;
		groom		= clone.groom;
	}
	
	@Override
	public ShipAttr clone() { return new ShipAttr(this); }
	
	public ShipPo templet(){ return templet; }

	@Override
	public byte[] toAttachBytes() {
		ByteBuf buf = Unpooled.buffer(  );
		Lua lua 	= LuaUtil.getDatabaseBufferForm();
		lua.getField( "shipAttr_ToBytes" ).call( 0, version, buf, this );
		return buf.array();
	}

	@Override
	public void wrapAttachBytes( byte[] bytes ) {
		if( bytes == null )
			return;
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		Lua lua 	= LuaUtil.getDatabaseBufferForm();
		lua.getField( "shipAttr_WrapBytes" ).call( 0, buf, this );
	}
	
	@Override
	public void randomAttachAttr() {
		Lua lua = LuaUtil.getGameData();
		lua.getField( "randomAttachAttr" ).call( 0, this );
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( maxHp );
		buffer.writeInt( maxEnergy );
		buffer.writeInt( mass );
	}

	public int getMaxHp() {
		return maxHp;
	}
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}
	public int getMaxEnergy() {
		return maxEnergy;
	}
	public void setMaxEnergy(int maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	public int getMass() {
		return mass;
	}
	public void setMass(int mass) {
		this.mass = mass;
	}
	public short getWroom() {
		return wroom;
	}
	public void setWroom(short wroom) {
		this.wroom = wroom;
	}
	public short getEroom() {
		return eroom;
	}
	public void setEroom(short eroom) {
		this.eroom = eroom;
	}
	public short getGroom() {
		return groom;
	}
	public void setGroom(short groom) {
		this.groom = groom;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	
	public static void main(String[] args) {
		Lua.setLogClass( Logs.class );
		CsvGen.load();
		ItemPo item = CsvGen.getItemPo( 20001 );
		
		ShipAttr attr = new ShipAttr( item, 1, item.id, 1, Quality.COLOR01 );
		attr.randomAttachAttr();
		
		System.out.println( "1----- maxHp=" + attr.getMaxHp() + ", maxEnergy=" + attr.getMaxEnergy() );
		
		byte[] bytes = attr.toAttachBytes();
		
		attr = new ShipAttr( item, 1, item.id, 1, Quality.COLOR01 );
		System.out.println( "2----- maxHp=" + attr.getMaxHp() + ", maxEnergy=" + attr.getMaxEnergy() );
		
		attr.wrapAttachBytes(bytes);
		System.out.println( "3----- maxHp=" + attr.getMaxHp() + ", maxEnergy=" + attr.getMaxEnergy() );
	}



}
