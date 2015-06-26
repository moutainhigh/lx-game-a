package cn.xgame.a.world.planet.data.specialty;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Material;

/**
 * 特产
 * @author deng		
 * @date 2015-6-26 下午12:07:42
 */
public class Specialty implements IBufferStream,ITransformStream{

	private Material templet;
	
	// 产出时间
	private int yieldTime;
	
	// 产量
	private int yieldNum;
	
	
	public Specialty( int id ) {
		templet = new Material( CsvGen.getMaterial(id) );
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt( yieldTime );
		buf.writeInt( yieldNum );
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		yieldTime = buf.readInt();
		yieldNum = buf.readInt();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( templet.id );
		buffer.writeInt( yieldTime );
		buffer.writeInt( yieldNum );
	}
	
	public Material templet() {
		return templet;
	}
	public int getYieldTime() {
		return yieldTime;
	}
	public void setYieldTime(int yieldTime) {
		this.yieldTime = yieldTime;
	}
	public int getYieldNum() {
		return yieldNum;
	}
	public void setYieldNum(int yieldNum) {
		this.yieldNum = yieldNum;
	}






}
