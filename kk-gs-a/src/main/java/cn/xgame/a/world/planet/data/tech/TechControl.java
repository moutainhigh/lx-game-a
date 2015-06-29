package cn.xgame.a.world.planet.data.tech;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;

/**
 * 科技 操作中心
 * @author deng		
 * @date 2015-6-26 下午1:32:00
 */
public class TechControl implements IArrayStream,ITransformStream{

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return ;
		
	}

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( 0 );
	}

}
