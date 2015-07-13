package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.a.world.planet.data.ectype.EctypeLists;
import cn.xgame.net.event.IEvent;

/**
 * 申请某星球的副本列表
 * @author deng		
 * @date 2015-7-13 下午5:50:18
 */
public class ApplyEctypeListEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int id = data.readInt();
		
		IPlanet planet = WorldManager.o.getPlanet( id );
		if( planet == null ) return;
		
		List<EctypeLists> ectypes = planet.getEctypeControl().getAllEctype();
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		response.writeShort( ectypes.size() );
		for( EctypeLists ectype : ectypes ){
			ectype.buildTransformStream(response);
			response.writeByte( 0 );
		}
		sendPackage( player.getCtx(), response );
	}

}
