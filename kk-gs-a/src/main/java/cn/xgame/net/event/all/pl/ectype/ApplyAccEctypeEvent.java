package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;


import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请偶发副本
 * @author deng		
 * @date 2015-7-20 上午10:12:10
 */
public class ApplyAccEctypeEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		player.getAccEctypes().buildTransformStream( response );
		sendPackage( player.getCtx(), response );
	}

}
