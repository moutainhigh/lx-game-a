package cn.xgame.a.prop;

import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.u.Player;
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

	
	protected void initialize(int uid, int nid, int count ) {
		this.uId = uid;
		this.nId = nid;
		this.count = count;
	}
	
	/**
	 * 写入基础数据到buffer
	 * @param buffer
	 */
	public void putBaseBuffer( ByteBuf buffer ) {
		buffer.writeInt(uId);
		buffer.writeInt(nId);
		buffer.writeInt(count);
	}
	
	public abstract PropType type();
	public abstract void createDB( Player player );
	public abstract void updateDB( Player player );
	
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
	
	public String toString(){
		return type().name() + ", uId=" + uId + ", nId=" + nId + ", count=" + count; 
	}
}