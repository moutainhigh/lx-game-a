package cn.xgame.a.world.planet.data.specialty;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.prop.IProp;
import cn.xgame.utils.Logs;

/**
 * 特产
 * @author deng		
 * @date 2015-6-26 下午12:07:42
 */
public class Specialty implements ITransformStream{

	// 
	volatile private IProp prop;
	
	// 产出时间
	volatile private int yieldTime;
	// 产量
	volatile private int yieldNum;
	
	// 记录时间
	volatile private int rtime;
	
	/**
	 * 创建一个特产
	 * @param id 特产表格ID
	 * @param yieldTime 产出时间 间隔
	 * @param yieldNum 一次产出的数量
	 */
	public Specialty( int id, int yieldTime, int yieldNum ) {
		this.prop		= IProp.create( 1, id, 0 );
		this.rtime		= (int) (System.currentTimeMillis()/1000);
		this.yieldTime 	= yieldTime;
		this.yieldNum 	= yieldNum;
	}

	/**
	 * 从数据库获取
	 * @param buf
	 */
	public Specialty( ByteBuf buf ) {
		this.prop		= IProp.create( 1, buf.readInt(), buf.readInt() );
		this.rtime		= (int) (System.currentTimeMillis()/1000);
		this.yieldTime 	= buf.readInt();
		this.yieldNum 	= buf.readInt();
	}

	public void putBuffer( ByteBuf buf ) {
		buf.writeInt( prop.getNid() );
		buf.writeInt( prop.getCount() );
		buf.writeInt( yieldTime );
		buf.writeInt( yieldNum );
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( prop.getNid() );
		buffer.writeInt( prop.getCount() );
	}
	
	
	/**
	 * 线程
	 */
	public boolean run(){
		
		if( prop.getCount() >= prop.getMaxOverlap() )
			return false;
		
		int gobyTime = (int) (System.currentTimeMillis()/1000) - rtime;
		if( gobyTime >= yieldTime ){
			rtime = (int) (System.currentTimeMillis()/1000);
			
			prop.addCount( yieldNum );
			
			Logs.debug( "星球特产"+prop.getNid()+" 生产 "+prop.getCount()+"个" );
			return true;
		}
		return false;
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
	public IProp toProp() {
		return prop;
	}


}
