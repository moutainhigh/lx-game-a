package cn.xgame.net.event.all.user;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.List;


import cn.xgame.logic.gs.GSData;
import cn.xgame.logic.gs.GSManager;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;

/**
 * 服务器 列表
 * @author deng		
 * @date 2015-6-16 上午9:46:57
 */
public class GslistEvent extends IEvent{

	@Override
	public void run( ChannelHandlerContext ctx, ByteBuf data ) throws IOException {
		
		// 暂时空包
		//..
		
		
		// 
		ByteBuf respond = buildEmptyPackage( ctx, 1024 );
		
		// 获取 开启服务器 列表
		List<GSData> ls = GSManager.o.getOpenGs();
		
		respond.writeShort( ls.size() );
		for( GSData gs : ls ){
			respond.writeShort( gs.getId() );
			RW.writeString( respond, gs.getName() );
			RW.writeString( respond, gs.getIp() );
			respond.writeInt( gs.getPort() );
			respond.writeByte( gs.getCurrentStatus() );// 服务器 状态 0.流畅 1.拥挤 2.爆满
		}
		sendPackage( ctx, respond );
		
	}

}
