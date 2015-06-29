package cn.xgame.a.world.planet.data.specialty;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Material;
import cn.xgame.utils.Logs;

/**
 * 特产
 * @author deng		
 * @date 2015-6-26 下午12:07:42
 */
public class Specialty implements IBufferStream,ITransformStream{

	private final Material templet;
	
	// 产出时间
	volatile private int yieldTime;
	// 产量
	volatile private int yieldNum;
	// 最高产出量
	volatile private int maxYieldNum;
	// 当前产出数量
	volatile private int yieldCount;
	
	// 记录时间
	volatile private int rtime;
	
	public Specialty( int id ) {
		templet = CsvGen.getMaterial(id);
		rtime	= (int) (System.currentTimeMillis()/1000);
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt( yieldTime );
		buf.writeInt( yieldNum );
		buf.writeInt( maxYieldNum );
		buf.writeInt( yieldCount );
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		yieldTime 	= buf.readInt();
		yieldNum 	= buf.readInt();
		maxYieldNum = buf.readInt();
		yieldCount 	= buf.readInt();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( templet.id );
		buffer.writeInt( yieldCount );
	}
	
	
	/**
	 * 线程
	 */
	public boolean run(){
		
		if( yieldCount >= maxYieldNum )
			return false;
		
		int gobyTime = (int) (System.currentTimeMillis()/1000) - rtime;
		if( gobyTime >= yieldTime ){
			rtime = (int) (System.currentTimeMillis()/1000);
			
			yieldCount += yieldNum;
			if( yieldCount > maxYieldNum )
				yieldCount = maxYieldNum;
			
			Logs.debug( "星球特产线程 yieldCount=" + yieldCount );
			return true;
		}
		return false;
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
	public int getYieldCount() {
		return yieldCount;
	}
	public void setYieldCount(int yieldCount) {
		this.yieldCount = yieldCount;
	}
	public int getMaxYieldNum() {
		return maxYieldNum;
	}
	public void setMaxYieldNum(int maxYieldNum) {
		this.maxYieldNum = maxYieldNum;
	}






}
