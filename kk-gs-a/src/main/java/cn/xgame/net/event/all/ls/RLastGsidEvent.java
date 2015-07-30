package cn.xgame.net.event.all.ls;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AbstractChannel;

import java.io.IOException;

import cn.xgame.a.Launch.LSClientAgency;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.system.SystemCfg;
import cn.xgame.utils.Logs;

/**
 * 记录 玩家最后一次 登录的服务器ID
 * @author deng		
 * @date 2015-6-24 下午3:20:04
 */
public class RLastGsidEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	public void run( String uid ) {
		
		try {
			AbstractChannel socket = LSClientAgency.socket();
			ByteBuf buffer = buildEmptyPackage( socket, 16 );
			
			RW.writeString( buffer, uid );
			buffer.writeShort( SystemCfg.ID );
			
			sendPackage( socket, buffer );
		} catch (IOException e) {
			Logs.error( "RLastGsidEvent", e );
		}
	}

	
}
