package cn.xgame.a.player.fleet.status;

import x.javaplus.util.ErrorCode;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.other.IStatus;
import cn.xgame.a.player.fleet.other.StatusType;

/**
 * 空闲状态
 * @author deng		
 * @date 2015-9-11 下午12:27:01
 */
public class LeisureStatus extends IStatus{

	public LeisureStatus( StatusType type ) {
		super(type);
	}
	public LeisureStatus(StatusType type, ByteBuf buf) {
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
		throw new Exception( ErrorCode.SHIP_CANNOT_FIGHT.name() );
	}
	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

}
