package cn.xgame.a.player.fleet.status;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.other.IStatus;
import cn.xgame.a.player.fleet.other.StatusType;

/**
 * 悬停状态
 * @author deng		
 * @date 2015-9-11 上午12:36:49
 */
public class HoverStatus extends IStatus{

	public HoverStatus() {
		super( StatusType.HOVER );
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
		buffer.writeByte( type().toNumber() );
	}
	
	@Override
	public boolean isComplete() {
		return true;
	}



}
