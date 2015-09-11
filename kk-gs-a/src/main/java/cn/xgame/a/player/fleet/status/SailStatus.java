package cn.xgame.a.player.fleet.status;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.other.IStatus;
import cn.xgame.a.player.fleet.other.StatusType;

/**
 * 航行状态
 * @author deng		
 * @date 2015-9-11 上午12:37:43
 */
public class SailStatus extends IStatus{

	@Override
	public StatusType type() { return StatusType.SAIL; }

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}

}
