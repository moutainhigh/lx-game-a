package cn.xgame.a.award;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;

/**
 * 一个奖励信息
 * @author deng		
 * @date 2015-7-30 下午11:13:49
 */
public class AwardInfo implements ITransformStream{

	// 道具表格ID
	private int id;
	
	// 道具数量
	private int count;
	
	public AwardInfo(int id, int count) {
		this.id 	= id;
		this.count 	= count;
	}

	public AwardInfo(ByteBuf buf) {
		this.id 	= buf.readInt();
		this.count 	= buf.readInt();
	}

	public String toString(){
		return id + "-" + count;
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( id );
		buffer.writeInt( count );
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}


}
