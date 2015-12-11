package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;


import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.info.IBuilding;
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
		
		ManorControl manors = player.getManors();
		manors.update();
		List<IBuilding> builds = manors.getBuilds();
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeByte( builds.size() );
		for( IBuilding o : builds ){
			buffer.writeInt( o.getNid() );
			buffer.writeByte( o.getIndex() );
			buffer.writeByte( o.getStatus().ordinal() );
			buffer.writeInt( o.getEndtime() );
		}
		sendPackage( player.getCtx(), buffer );
	}

}
