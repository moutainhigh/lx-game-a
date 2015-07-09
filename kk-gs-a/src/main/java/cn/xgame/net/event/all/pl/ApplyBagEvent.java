package cn.xgame.net.event.all.pl;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请背包基础数据
 * @author deng		
 * @date 2015-7-8 上午11:09:56
 */
public class ApplyBagEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		player.getProps().buildTransformStream(response);
		sendPackage( player.getCtx(), response );
	}

}
