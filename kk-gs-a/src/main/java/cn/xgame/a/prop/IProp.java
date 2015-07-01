package cn.xgame.a.prop;

import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Item;
import io.netty.buffer.ByteBuf;

/**
 * 道具 基类
 * @author deng		
 * @date 2015-6-17 下午7:02:11
 */
public abstract class IProp implements ITransformStream{
	
	// 基础物品表
	private final Item item;
	
	// 唯一ID
	private int uId;
	// 表格ID
	private int nId;
	// 数量
	private int count;
	
	public IProp( int uid, int nid, int count ){
		this.uId 	= uid;
		this.nId 	= nid;
		this.item 	= CsvGen.getItem(nid);
		addCount( count );
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
	
	/** 获取这个物品的贡献度 */
	public int getContributions() {
		return item.sellgold == 0 ? 1 : item.sellgold * 5;
	}
	
	/**
	 * 是否可以累加
	 * @return
	 */
	public boolean isCanAcc() {
		return count < item.manymax;
	}
	
	public abstract PropType type();
	public abstract void createDB( Player player );
	public abstract void updateDB( Player player );
	public abstract void deleteDB( Player player );
	
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
	public int addCount(int count) {
		int ret = 0;
		if( (this.count+=count) > item.manymax ){
			ret = (int) (this.count - item.manymax);
			this.count = (int) item.manymax;
		}
		return ret;
	}
	public Item item(){ return item; }
	
	public String toString(){
		return type().name() + ", uId=" + uId + ", nId=" + nId + ", count=" + count; 
	}



}
