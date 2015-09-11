package cn.xgame.a.player.fleet.status;

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
	public void buildTransformStream(ByteBuf buffer) {
		putBuffer( buffer );
	}

}
