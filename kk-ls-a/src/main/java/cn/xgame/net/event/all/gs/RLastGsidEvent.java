package cn.xgame.net.event.all.gs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import cn.xgame.a.user.UserManager;
import cn.xgame.net.netty.classes.IEvent;
import cn.xgame.net.netty.classes.Netty.RW;

/**
 * 记录用户 最后一次登录的服务器ID
 * @author deng		
 * @date 2015-6-24 下午3:28:38
 */
public class RLastGsidEvent extends IEvent{

	@Override
	public void run(ChannelHandlerContext ctx, ByteBuf data) throws IOException {
		
		String uid 	= RW.readString(data);
		short gsid	= data.readShort();
		
		UserManager.o.updateLastGsid( uid, gsid );
	}

}
