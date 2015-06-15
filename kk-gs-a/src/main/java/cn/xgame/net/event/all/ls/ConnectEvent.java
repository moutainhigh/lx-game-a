package cn.xgame.net.event.all.ls;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AbstractChannel;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.Launch;
import cn.xgame.a.system.SystemCfg;
import cn.xgame.logic.player.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;

public class ConnectEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		ErrorCode code = ErrorCode.fromNum( data.readShort() );
		
		Launch.handleConnect( code );
	}

	// 发送 登录
	public void run( AbstractChannel socket ) throws IOException {
		
		ByteBuf buffer = buildEmptyPackage(socket, 1);
		
		buffer.writeShort( SystemCfg.ID );
		RW.writeString( buffer, SystemCfg.GS_NAME );
		buffer.writeInt( SystemCfg.GS_PORT );
		
		sendPackage( socket, buffer );
	}

	
}
