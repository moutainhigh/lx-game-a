package cn.xgame.a.player.prop;

import cn.xgame.a.player.ITransformStream;
import io.netty.buffer.ByteBuf;

/**
 * 道具 基类
 * @author deng		
 * @date 2015-6-17 下午7:02:11
 */
public abstract class IProp implements ITransformStream{
	
	// 唯一ID
	private int uId;
	// 表格ID
	private int nId;
	// 数量
	private int count;
	
	
	public int getuId() {
		return uId;
	}
	public void setuId(int uId) {
		this.uId = uId;
	}
	public int getnId() {
		return nId;
	}
	public void setnId(int nId) {
		this.nId = nId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * 塞入数据到buffer
	 * @param buffer
	 */
	public void putBuffer( ByteBuf buffer ){
		uId = buffer.readInt();
		nId = buffer.readInt();
		count = buffer.readInt();
	}
	
	/**
	 * 写入数据到buffer
	 * @param buffer
	 */
	public void toBuffer( ByteBuf buffer ) {
		buffer.writeInt(uId);
		buffer.writeInt(nId);
		buffer.writeInt(count);
	}
	
	
}
