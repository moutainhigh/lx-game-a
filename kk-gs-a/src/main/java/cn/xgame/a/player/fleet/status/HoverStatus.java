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

	public HoverStatus(StatusType type) {
		super(type);
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
		
	}
	
	@Override
	public boolean canFighting() throws Exception {
		return true;
	}
	
	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}



}
