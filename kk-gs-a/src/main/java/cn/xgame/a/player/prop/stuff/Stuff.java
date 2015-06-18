package cn.xgame.a.player.prop.stuff;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.prop.IProp;

/**
 * 材料对象
 * @author deng		
 * @date 2015-6-17 下午7:46:35
 */
public class Stuff extends IProp{

	
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		super.toBuffer(buffer);
		
		
	}

}
