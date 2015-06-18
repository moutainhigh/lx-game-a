package cn.xgame.net.event.all.ls;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AbstractChannel;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.Launch;
import cn.xgame.a.Launch.LSClientAgency;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.system.SystemCfg;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;

public class ConnectEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		ErrorCode code = ErrorCode.fromNum( data.readShort() );
		
		Launch.handleConnect( code );
	}

	// 发送 登录
	public void run(  ) throws IOException {
		
		AbstractChannel socket = LSClientAgency.socket();
		
		ByteBuf buffer = buildEmptyPackage( socket, 16 );
		
		buffer.writeShort( SystemCfg.ID );
		RW.writeString( buffer, SystemCfg.GS_NAME );
		buffer.writeInt( SystemCfg.GS_PORT );
		
		sendPackage( socket, buffer );
	}

	
}
