package cn.xgame.net.event.all;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.net.event.IEvent;
import cn.xgame.user.Player;


public class TestEevet extends IEvent{

	@Override
	public void run( Player player, ByteBuf data ) throws IOException {
		
	}

}
