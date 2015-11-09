package cn.xgame.net.event.all.pl.transaction;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;


import cn.xgame.a.player.swop.info.SpecialSwop;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请特殊兑换列表
 * @author deng		
 * @date 2015-11-7 上午11:33:27
 */
public class ApplySSwopEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int sid = data.readInt();
		
		List<SpecialSwop> ss = player.getSwops().getSSwops( sid );
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 20 );
		buffer.writeByte( ss.size() );
		for( SpecialSwop s : ss ){
			buffer.writeInt( s.getId() );
			buffer.writeByte( s.getTimes() );
		}
		sendPackage( player.getCtx(), buffer );
		
	}

}
