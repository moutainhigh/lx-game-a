package cn.xgame.a.player.fleet.status;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.other.IStatus;
import cn.xgame.a.player.fleet.other.StatusType;

/**
 * 战斗状态
 * @author deng		
 * @date 2015-9-11 上午12:39:04
 */
public class CombatStatus extends IStatus{

	public CombatStatus(StatusType type) {
		super(type);
	}
	public CombatStatus(StatusType type, ByteBuf buf) {
		super(type);
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		putBuffer( buffer );
		
		
	}

}
