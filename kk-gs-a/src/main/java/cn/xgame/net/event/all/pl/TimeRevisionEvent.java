package cn.xgame.net.event.all.pl;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;

/**
 * 时间校对
 * @author deng		
 * @date 2015-12-13 下午6:37:35
 */
public class TimeRevisionEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {

		double ctime = data.readDouble();
		double stime = System.currentTimeMillis();
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 32 );
		buffer.writeDouble( ctime );
		buffer.writeDouble( stime );
		sendPackage( player.getCtx(), buffer );
		
		Logs.debug( player.getCtx(), "时间校对 c_time="+(long)ctime+", gs_time="+(long)stime+", delta="+(long)(stime-ctime) );
	}

}
