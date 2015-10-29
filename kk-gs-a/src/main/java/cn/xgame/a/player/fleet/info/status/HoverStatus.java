package cn.xgame.a.player.fleet.info.status;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.classes.IStatus;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;

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
		super.putBuffer(buf);
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		super.wrapBuffer(buf);
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		super.buildTransformStream(buffer);
	}
	
	@Override
	public boolean isComplete() {
		return true;
	}

	@Override
	public void execut(FleetInfo fleetInfo, Player player) {
		
	}

}
