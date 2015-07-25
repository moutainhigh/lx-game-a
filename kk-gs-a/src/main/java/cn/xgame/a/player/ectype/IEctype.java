package cn.xgame.a.player.ectype;

import io.netty.buffer.ByteBuf;
import cn.xgame.config.o.Ectype;

/**
 * 副本基类
 * @author deng		
 * @date 2015-7-25 下午12:57:43
 */
public abstract class IEctype {
	
	protected final Ectype template;
	
	// 所属星球ID
	protected final int snid ;
	
	// 副本类型
	protected EctypeType type;
	
	
	/**
	 * 从配置表获取
	 * @param id 所属星球ID
	 * @param src
	 */
	public IEctype( int id, Ectype src ) {
		snid 		= id;
		template 	= src;
		type		= EctypeType.fromNumber( template.type );
	}

	/**
	 * 返回到数据库
	 * @param buffer
	 */
	public void putBuffer(ByteBuf buffer) {
		buffer.writeInt( snid );
		buffer.writeInt( template.id );
	}

	/**
	 * 该副本是否关闭
	 * @return
	 */
	public abstract boolean isClose();
	public Ectype template(){ return template; }
	public int getNid() { return template.id; }
	public EctypeType type(){ return type ; }
	
}
