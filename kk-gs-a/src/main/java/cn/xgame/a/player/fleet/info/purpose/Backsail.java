package cn.xgame.a.player.fleet.info.purpose;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.classes.IPurpose;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;

/**
 * 返航
 * @author deng		
 * @date 2015-10-30 上午1:23:44
 */
public class Backsail extends IPurpose{

	public Backsail() {
		super((byte) 3);
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
		buffer.writeByte( type() );
	}

	@Override
	public void execut( int endtime, int targetId, FleetInfo fleet, Player player ) {
		// 直接设置悬停
		fleet.changeStatus( StatusType.HOVER );
	}

}
