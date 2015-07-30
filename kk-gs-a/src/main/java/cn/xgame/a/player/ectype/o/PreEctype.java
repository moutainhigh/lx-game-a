package cn.xgame.a.player.ectype.o;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.ectype.IEctype;
import cn.xgame.config.o.Ectype;

/**
 * 一个常驻副本
 * @author deng		
 * @date 2015-7-25 下午1:05:03
 */
public class PreEctype extends IEctype implements ITransformStream{

	/**
	 * 从配置表获取
	 * @param id 所属星球ID
	 * @param src
	 */
	public PreEctype( int id, Ectype src ) {
		super( id, src );
	}
	
	/**
	 * 从数据库获取
	 * @param buf
	 */
	public PreEctype( ByteBuf buf ) {
		super( buf );
	}
	
	public String toString(){
		return "{" + template.id + "-" + SNID + "}";
	}
	
	@Override
	public boolean isClose() {
		return false;
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		putBuffer(buffer);
	}

}
