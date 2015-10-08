package cn.xgame.net.event.all.pl.transaction;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.data.exchange.ExchGoods;
import cn.xgame.a.world.planet.data.exchange.ExchangeControl;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;

/**
 * 交易所-下架
 * @author deng		
 * @date 2015-10-8 下午3:51:41
 */
public class ExchSoldoutEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int uid = data.readInt();
		
		ErrorCode code = null;
		List<IProp> ret = null;
		try {
			
			HomePlanet home = WorldManager.o.getHPlanetInPlayer(player);
			ExchangeControl exchange = home.getExchange();
			ExchGoods goods = exchange.getGoods( uid );
			if( goods == null )
				throw new Exception( ErrorCode.PROP_NOTEXIST.name() );
			
			// 直接从交易所删除掉
			exchange.removeGoods( goods );
			
			// 然后放回玩家背包
			ret = player.getDepots(home.getId()).appendProp( goods.getProp() );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf respon = buildEmptyPackage( player.getCtx(), 125 );
		respon.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			respon.writeInt( uid );
			respon.writeByte( ret.size() );
			for( IProp prop : ret ){
				prop.putBaseBuffer(respon);
				prop.buildTransformStream(respon);
			}
		}
		sendPackage( player.getCtx(), respon );
	}

}
