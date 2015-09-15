package cn.xgame.net.event.all.gs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.gs.GSManager;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;

/**
 * 游戏服务器 连接
 * @author deng		
 * @date 2015-6-12 下午4:03:08
 */
public class ConnectEvent extends IEvent{

	@Override
	public void run( ChannelHandlerContext ctx, ByteBuf data ) throws IOException {
		
		// 服务器 ID - 也是区id
		short gsid 	= data.readShort();
		// 服务器 名字 
		String name = RW.readString(data);
		// 服务器 IP
		String ip	= RW.readString(data);
		// 服务器 端口
		int port	= data.readInt();
		
		ErrorCode code = GSManager.o.connect( gsid, name, ip, port, ctx );
		
		ByteBuf respond = buildEmptyPackage( ctx, 2 );
		respond.writeShort( code.toNumber() );
		sendPackage( ctx, respond );
		
	}

}
