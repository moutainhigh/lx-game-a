package cn.xgame.net.event.all.pl;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 保存新手引导状态值
 * @author deng		
 * @date 2015-11-20 上午11:47:57
 */
public class NewbieguideEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		byte guidestatus = data.readByte();
		player.setGuideStatus(guidestatus);
		// 这里删除掉船
		if( guidestatus >= 5 ){
			FleetInfo fleetInfo = player.getFleets().getFleetHavaShip().get(0);
			player.getDocks().destroyShip( fleetInfo.getShips().get(0) );
			if( !player.getDocks().getApron().isEmpty() )
				fleetInfo.addShip( player.getDocks().getApron().get(0) );
		}
	}

}
