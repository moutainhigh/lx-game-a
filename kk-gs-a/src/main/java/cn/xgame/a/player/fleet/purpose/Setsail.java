package cn.xgame.a.player.fleet.purpose;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.other.IPurpose;

/**
 *  出航
 * @author deng		
 * @date 2015-9-14 下午12:34:01
 */
public class Setsail extends IPurpose{

	public Setsail( byte _type ) {
		super(_type);
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void wrapBuffer(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		putBuffer(buffer);
	}


}
