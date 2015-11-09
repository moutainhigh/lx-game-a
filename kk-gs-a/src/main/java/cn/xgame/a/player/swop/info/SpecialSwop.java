package cn.xgame.a.player.swop.info;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;

/**
 * 特殊兑换 
 * @author deng		
 * @date 2015-11-6 下午6:14:32
 */
public class SpecialSwop implements IBufferStream{
	
	// 所属星球ID
	private int sind;
	
	// 兑换道具ID
	private int id;
	
	// 当前使用次数
	private byte times;
	
	
	public SpecialSwop(int sid, int id ) {
		this.sind 	= sid;
		this.id 	= id;
		this.times 	= 0;
	}
	public SpecialSwop() {
	}
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt(sind);
		buf.writeInt(id);
		buf.writeByte(times);
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		this.sind 	= buf.readInt();
		this.id 	= buf.readInt();
		this.times 	= buf.readByte();
	}
	
	public int getTimes() {
		return times;
	}
	public int getId() {
		return id;
	}
	public int getSind() {
		return sind;
	}

	public void addTimes(int count) {
		this.times += count;
	}
}
