package cn.xgame.a.player.bag;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.ITransformStream;
import cn.xgame.a.player.prop.IProp;

/**
 * 一个小格子
 * @author deng		
 * @date 2015-6-17 下午6:51:55
 */
public class Bag extends IBag implements ITransformStream{
	
	// 对象
	private IProp prop;

	
	
	
	
	
	
	public IProp getProp() {
		return prop;
	}
	public void setProp(IProp prop) {
		this.prop = prop;
	}
	
	
	
	public void putBuffer( ByteBuf buffer ) {
		
		type = buffer.readByte();
		prop = newProp();
		prop.putBuffer( buffer );
	}
	
	public void toBuffer( ByteBuf buffer ){
		buffer.writeByte(type);
		prop.toBuffer( buffer );
	}
	
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		prop.buildTransformStream( buffer );
	}
	
	
}
