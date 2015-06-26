package cn.xgame.a.world.planet.data.building;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Building;

/**
 * 建筑
 * @author deng		
 * @date 2015-6-26 下午12:32:13
 */
public class Buildings implements IBufferStream,ITransformStream {

	private final Building templet;
	
	// 位置
	private short index;
	
	public Buildings( int id ) {
		templet = CsvGen.getBuilding(id);
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeShort( index );
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		index = buf.readShort();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( templet.id );
		buffer.writeShort( index );
	}
	
	public Building templet(){ return templet; }
	public short getIndex() {
		return index;
	}
	public void setIndex(short index) {
		this.index = index;
	}


}
