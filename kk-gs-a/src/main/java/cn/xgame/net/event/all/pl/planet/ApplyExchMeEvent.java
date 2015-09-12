package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.data.exchange.ExchGoods;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;

/**
 * 申请交易所自己的数据
 * @author deng		
 * @date 2015-8-31 上午11:37:54
 */
public class ApplyExchMeEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int page 			= data.readInt();
		
		List<ExchGoods> ret = Lists.newArrayList();
		try {
			HomePlanet home = WorldManager.o.getHPlanetInPlayer(player);
			
			ret.addAll( home.getExchange().getGoodsByPlayer( player, page ) );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ByteBuf response 	= buildEmptyPackage( player.getCtx(), 1024 );
		response.writeByte( ret.size() );
		for( ExchGoods goods : ret ){
			goods.buildTransformStream(response);
		}
		sendPackage( player.getCtx(), response );
		
	}

}
