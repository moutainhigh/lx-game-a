package cn.xgame.a.world.planet.data.exchange;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.netty.Netty.RW;

/**
 * 一个交易物品
 * @author deng		
 * @date 2015-8-30 下午2:27:15
 */
public class ExchGoods implements ITransformStream,IBufferStream{
	
	// 出售玩家唯一ID
	private String sellUid;
	// 出售玩家名字
	private String sellName;

	// 道具
	private IProp prop;
	
	// 单价
	private int unitprice;

	
	public String toString(){
		return sellName + "-" + prop.getUid() + "-" + unitprice;
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		RW.writeString(buf, sellUid);
		RW.writeString(buf, sellName);
		buf.writeInt(unitprice);
		prop.putBuffer(buf);
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		sellUid 	= RW.readString(buf);
		sellName 	= RW.readString(buf);
		unitprice	= buf.readInt();
		prop		= IProp.create( buf );
	}
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		RW.writeString(buffer, sellName);
		prop.putBaseBuffer(buffer);
		buffer.writeInt(unitprice);
	}
	
	/**
	 * 扣除道具个数
	 * @param count
	 */
	public boolean deduct( int num ) {
		prop.deductCount(num);
		return prop.isEmpty();
	}
	
	public IProp getProp() {
		return prop;
	}
	public void setProp(IProp prop) {
		this.prop = prop;
	}
	public String getSellUid() {
		return sellUid;
	}
	public void setSellUid(String sellUid) {
		this.sellUid = sellUid;
	}
	public String getSellName() {
		return sellName;
	}
	public void setSellName(String sellName) {
		this.sellName = sellName;
	}
	public int getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(int unitprice) {
		this.unitprice = unitprice;
	}
	public int getUid() {
		return prop.getUid();
	}
	public int getNid() {
		return prop.getNid();
	}
	public int getCount() {
		return prop.getCount();
	}





	
}
