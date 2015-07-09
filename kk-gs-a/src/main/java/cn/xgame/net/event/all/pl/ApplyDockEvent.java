package cn.xgame.net.event.all.pl;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请船坞详细数据
 * @author deng		
 * @date 2015-7-9 下午12:32:35
 */
public class ApplyDockEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		player.getDocks().buildTransformStream(response);
		sendPackage( player.getCtx(), response );
	}

}
