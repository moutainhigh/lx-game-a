package cn.xgame.a.prop.ship;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
	
	// 当前血量
	private int currentHp;
	// 最大血量
	private int maxHp;
	
	
	
	public ShipAttr( ItemPo item, int uid, int nid, int count) {
		super( item, uid, nid, count);
		templet 	= CsvGen.getShipPo(nid);
		currentHp	= templet.hp;
		maxHp		= templet.hp;
	}

	private ShipAttr( ShipAttr clone ){
		super( clone );
		templet 	= clone.templet;
		currentHp	= clone.currentHp;
		maxHp		= clone.maxHp;
	}
	
	@Override
	public ShipAttr clone() { return new ShipAttr(this); }
	
	public ShipPo templet(){ return templet; }

	@Override
	public byte[] toAttachBytes() {
		ByteBuf buf = Unpooled.buffer( 8 );
		buf.writeInt( currentHp );
		buf.writeInt( maxHp );
		return buf.array();
	}

	@Override
	public void wrapAttachBytes( byte[] bytes ) {
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		currentHp 	= buf.readInt();
		maxHp 		= buf.readInt();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}

	public int getCurrentHp() {
		return currentHp;
	}
	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}
	public int getMaxHp() {
		return maxHp;
	}
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}


}
