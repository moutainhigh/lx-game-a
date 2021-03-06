package cn.xgame.net.event.all.gs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import cn.xgame.a.gs.GSManager;
import cn.xgame.net.netty.classes.IEvent;
import cn.xgame.utils.Logs;

/**
 * 更新服务器 人数
 * @author deng		
 * @date 2015-6-16 上午10:19:07
 */
public class UpdatePeopleEvent extends IEvent{

	@Override
	public void run(ChannelHandlerContext ctx, ByteBuf data) throws IOException {
		
		short gsid 			= data.readShort();
		int onlinePeople 	= data.readInt();
		int peopleNum 		= data.readInt();
		
		GSManager.o.updatePeople( gsid, peopleNum, onlinePeople );
		
		// 暂时 不返回包
		Logs.debug( ctx, "服务器更新人数  peopleNum=" + peopleNum + ", onlinePeople=" + onlinePeople );
	}

}
