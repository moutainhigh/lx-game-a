package cn.xgame.a.player.fleet.info.result;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.classes.IResult;

/**
 * 结算中
 * @author deng		
 * @date 2015-11-3 下午5:58:01
 */
public class Settlement extends IResult{

	public Settlement() {
		super((byte) 2);
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
	public boolean isComplete() {
		return true;
	}

}
