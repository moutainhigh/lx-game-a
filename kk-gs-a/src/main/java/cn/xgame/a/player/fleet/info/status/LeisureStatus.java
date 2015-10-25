package cn.xgame.a.player.fleet.info.status;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.classes.IStatus;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;

/**
 * 空闲状态
 * @author deng		
 * @date 2015-9-11 下午12:27:01
 */
public class LeisureStatus extends IStatus{

	public LeisureStatus(  ) {
		super( StatusType.LEISURE );
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
		buffer.writeByte( type().toNumber() );
	}
	
	@Override
	public boolean isComplete() {
		return true;
	}
	
	@Override
	public IStatus execut(FleetInfo fleetInfo, Player player) {
		return this;
	}

}
