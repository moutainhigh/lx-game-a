package cn.xgame.net.event.all.ls;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AbstractChannel;

import java.io.IOException;

import cn.xgame.a.Launch.LSClientAgency;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.system.SystemCfg;
import cn.xgame.net.event.IEvent;

/**
 * 更新服务器 人数
 * @author deng		
 * @date 2015-6-16 上午10:19:07
 */
public class UpdatePeopleEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	public void run() throws IOException {
		
		AbstractChannel socket = LSClientAgency.socket();
		
		ByteBuf buffer = buildEmptyPackage( socket, 6 );
		
		buffer.writeShort( SystemCfg.ID );
		buffer.writeInt( PlayerManager.o.peopleNumber() );
		
		sendPackage( socket, buffer );
		
	}
	
	
	
	
	
	
	

}
