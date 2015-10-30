package cn.xgame.net.event.all.pl.fleet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 踢人
 * @author deng		
 * @date 2015-10-30 下午5:52:51
 */
public class KickPlayerEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
	}

}
