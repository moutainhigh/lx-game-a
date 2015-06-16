package cn.xgame.net.event.all.pl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import cn.xgame.logic.player.Player;
import cn.xgame.net.event.IEvent;

public class CreateEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void run( ChannelHandlerContext ctx, ByteBuf data ) {
		// TODO Auto-generated method stub
		
	}

}
