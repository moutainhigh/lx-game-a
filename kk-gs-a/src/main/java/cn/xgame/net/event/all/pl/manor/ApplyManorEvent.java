package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;


import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请领地
 * @author deng		
 * @date 2015-10-8 下午2:21:19
 */
public class ApplyManorEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte No = data.readByte();
		
		ManorControl manors = player.getManors();
		manors.update();
		
		ByteBuf buff = buildEmptyPackage( player.getCtx(), 125 );
		buff.writeByte( No );
		manors.buildTransformStream(buff);
		sendPackage( player.getCtx(), buff );
	}

}
