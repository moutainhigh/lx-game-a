package cn.xgame.net.event.all.pl.staratlas;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.net.event.IEvent;

/**
 * 申请星图
 * @author deng		
 * @date 2015-7-16 下午3:59:06
 */
public class ApplyStaratlasEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		WorldManager.o.putStaratlas( response );
		sendPackage( player.getCtx(), response );
	}

}
