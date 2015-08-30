package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;


import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.PropType;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.data.exchange.ExchGoods;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;

/**
 * 申请交易所数据
 * @author deng		
 * @date 2015-8-30 下午3:01:55
 */
public class ApplyExchEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte type		= data.readByte();
		int page 		= data.readInt();
		
		HomePlanet home = WorldManager.o.getHPlanetInPlayer(player);
		
		ByteBuf response 	= buildEmptyPackage( player.getCtx(), 1024 );
		List<ExchGoods> ls 	= home.getExchange().getGoodsByPage( PropType.fromNumber(type), page );
		response.writeByte( ls.size() );
		for( ExchGoods goods : ls ){
			goods.buildTransformStream(response);
		}
		sendPackage( player.getCtx(), response );
	}

}
