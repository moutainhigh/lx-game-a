package cn.xgame.a.player.ectype.o;

import x.javaplus.util.Util.Time;
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
	// 使用次数
	private int times = -1;
	// 结束时间
	private int endTime = -1;
	
	//--------- 临时数据
	
	// 副本类型
	private AccType type;
	// 持续时间 单位秒
	private int persistTime = -1;
	
	public String toString(){
		return "{" + template.id + "-" + snid + ", times:" + times + ", endTime:" + Time.refFormatDate( endTime * 1000l, "HH:mm:ss" ) + "}";
	}
	
	/**
	 * 从配置表获取
	 * @param id 所属星球ID
	 * @param src
	 */
	public AccEctype( int id, Ectype src ) {
		snid 		= id;
		template 	= src;
		init();
	}

	private void init() {
		type		= AccType.fromNumber( template.type );
		
		if( template.times != -1 && times == -1 )// 这里说明是需要次数的
			times = 0;
		
		if( type == AccType.LOGINTIME ){// 登录计时
			persistTime = Integer.parseInt( template.etime );
		}else if( type == AccType.SERVERTIME ){// 服务器计时
			String[] x 	= template.etime.split(";");
			persistTime = Integer.parseInt( x[1] );
			if( endTime == -1 )
				endTime	= (int) (Time.wrapTime( x[0] )/1000 + persistTime);
		}
	}
	
	/**
	 * 从数据库获取
	 * @param buf
	 */
	public AccEctype(ByteBuf buf) {
		snid = buf.readInt();
		template = CsvGen.getEctype( buf.readInt() );
		times = buf.readInt();
		endTime = buf.readInt();
		init();
	}
	
	/**
	 * 返回到数据库
	 * @param buffer
	 */
	public void putBuffer(ByteBuf buffer) {
		buffer.writeInt( snid );
		buffer.writeInt( template.id );
		buffer.writeInt( times );
		buffer.writeInt( endTime );
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		putBuffer( buffer );
	}

	/**
	 * 该副本是否关闭
	 * @return
	 */
	public boolean isClose() {
		if( endTime == -1 )
			return false;
		// 当前时间 大于 结束时间 表示该副本已经结束
		return System.currentTimeMillis()/1000 >= endTime;
	}
	
	public Ectype template(){ return template; }
	public int getNid() { return template.id; }
	public AccType type(){ return type ; }
	
	public int getTimes() {
		return times;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime() {
		endTime = (int) (System.currentTimeMillis()/1000 + persistTime);
	}

}
