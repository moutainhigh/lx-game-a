package cn.xgame.a.world.planet.data.specialty;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ItemPo;
import cn.xgame.utils.Logs;

/**
 * 特产
 * @author deng		
 * @date 2015-6-26 下午12:07:42
 */
public class Specialty implements IBufferStream,ITransformStream{

	private final ItemPo templet;
	
	// 产出时间
	volatile private int yieldTime;
	// 产量
	volatile private int yieldNum;
	// 当前产出数量
	volatile private int yieldCount;
	
	// 记录时间
	volatile private int rtime;
	
	public Specialty( int id ) {
		templet = CsvGen.getItemPo(id);
		rtime	= (int) (System.currentTimeMillis()/1000);
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt( yieldTime );
		buf.writeInt( yieldNum );
		buf.writeInt( yieldCount );
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		yieldTime 	= buf.readInt();
		yieldNum 	= buf.readInt();
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
		
		if( yieldCount >= templet.manymax )
			return false;
		
		int gobyTime = (int) (System.currentTimeMillis()/1000) - rtime;
		if( gobyTime >= yieldTime ){
			rtime = (int) (System.currentTimeMillis()/1000);
			
			yieldCount += yieldNum;
			if( yieldCount > templet.manymax )
				yieldCount = templet.manymax;
			
			Logs.debug( "星球特产"+templet.id+" 生产 " + yieldCount + "个" );
			return true;
		}
		return false;
	}
	
	public ItemPo templet() {
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

	public IProp toProp() {
		PropType type = PropType.fromNumber( templet.bagtype );
		return type.create( 1, templet.id, yieldCount );
	}






}
