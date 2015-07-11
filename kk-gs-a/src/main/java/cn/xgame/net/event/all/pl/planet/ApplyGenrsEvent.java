package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.net.event.IEvent;

/**
 * 申请某星球的所有元老数据
 * @author deng		
 * @date 2015-7-10 下午4:26:05
 */
public class ApplyGenrsEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		short nid = data.readShort();
		
		List<Child> ls = null;
		IPlanet planet = WorldManager.o.getPlanet( nid );
		if( planet != null ) 
			ls = planet.getAllGenrs();
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		response.writeByte( ls == null ? 0 : ls.size() );
		if( ls != null ){
			for( Child o : ls ){
				o.buildTransformStream(response);
			}
		}
		sendPackage( player.getCtx(), response );
		
	}

}
