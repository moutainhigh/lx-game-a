package cn.xgame.a.player.ectype.o;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Ectype;

/**
 * 一个偶发副本
 * @author deng		
 * @date 2015-7-17 上午3:27:20
 */
public class AccEctype implements ITransformStream{
	
	private final Ectype template;
	
	// 所属星球ID
	private final int snid ;
	// 剩余次数
	private int times = -1;
	// 剩余时间
	private int rtime = -1;
	
	/**
	 * 从配置表获取
	 * @param id 所属星球ID
	 * @param src
	 */
	public AccEctype( int id, Ectype src ) {
		snid = id;
		template = src;
	}
	
	/**
	 * 从数据库获取
	 * @param buf
	 */
	public AccEctype(ByteBuf buf) {
		snid = buf.readInt();
		template = CsvGen.getEctype( buf.readInt() );
		times = buf.readInt();
		rtime = buf.readInt();
	}

	public Ectype template(){ return template; }
	public int getNid() { return template.id; }

	
	public int getTimes() {
		return times;
	}

	public int getRTime() {
		return rtime;
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( snid );
		buffer.writeInt( template.id );
		buffer.writeInt( times );
		buffer.writeInt( rtime );
	}

}
