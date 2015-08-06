package cn.xgame.net.event.all.user;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.List;


import cn.xgame.a.gs.GSData;
import cn.xgame.a.gs.GSManager;
import cn.xgame.a.user.UserManager;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;

/**
 * 服务器 列表
 * @author deng		
 * @date 2015-6-16 上午9:46:57
 */
public class ApplyGslistEvent extends IEvent{

	@Override
	public void run( ChannelHandlerContext ctx, ByteBuf data ) throws IOException {
		
		String UID = RW.readString(data);
		
		
		
		// 获取 用户最后一次 登录的服务器ID
		short lastGsid 	= UserManager.o.getLastGsid( UID );
		
		// 
		ByteBuf respond = buildEmptyPackage( ctx, 1024 );
		respond.writeShort( lastGsid );
		respond.writeShort( GSManager.o.getRecommendGsid() );
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
