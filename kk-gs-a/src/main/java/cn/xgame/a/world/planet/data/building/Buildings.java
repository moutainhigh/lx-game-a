package cn.xgame.a.world.planet.data.building;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Sbuilding;

/**
 * 建筑
 * @author deng		
 * @date 2015-6-26 下午12:32:13
 */
public class Buildings implements IBufferStream,ITransformStream {

	private final Sbuilding templet;
	
	// 位置
	private byte index;
	
	public Buildings( int id ) {
		templet = CsvGen.getSbuilding(id);
	}
	
	public Buildings( Buildings src ) {
		this.templet = src.templet;
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeByte( index );
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		index = buf.readByte();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( templet.id );
		buffer.writeByte( index );
	}
	
	public Sbuilding templet(){ return templet; }
	public byte getIndex() {
		return index;
	}
	public void setIndex(byte index) {
		this.index = index;
	}

	/**
	 * 该位置 是否重叠
	 * @param index
	 * @param room
	 * @return
	 */
	public boolean indexIsOverlap( byte oindex, byte oroom ) {
		return oindex < index+templet.usegrid && oindex+oroom > index;
	}


}
