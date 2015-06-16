package cn.xgame.net.event.all.pl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import cn.xgame.logic.player.Player;
import cn.xgame.net.event.IEvent;

/**
 * 玩家登录 
 * @author deng		
 * @date 2015-6-15 下午4:49:21
 */
public class LoginEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	public void run( ChannelHandlerContext ctx, ByteBuf data ) {
		
		
		
		
		
		
	}


}
