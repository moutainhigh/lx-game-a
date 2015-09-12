package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;


import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.PropType;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.data.exchange.ExchGoods;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;

/**
 * 申请交易所数据
 * @author deng		
 * @date 2015-8-30 下午3:01:55
 */
public class ApplyExchEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte type			= data.readByte();
		int page 			= data.readInt();
		
		List<ExchGoods> ret = Lists.newArrayList();
		try {
			
			HomePlanet home = WorldManager.o.getHPlanetInPlayer(player);
			
			ret.addAll( home.getExchange().getGoodsByPage( PropType.fromNumber(type), page ) );
			
		} catch (Exception e) {
			
		}
		
		ByteBuf response 	= buildEmptyPackage( player.getCtx(), 1024 );
		response.writeByte( ret.size() );
		for( ExchGoods goods : ret ){
			RW.writeString( response, goods.getSellName() );
			goods.buildTransformStream(response);
		}
		sendPackage( player.getCtx(), response );
	}

}
