package cn.xgame.net.event.all.pl.transaction;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.data.exchange.ExchGoods;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;

/**
 * 交易所-购买
 * @author deng		
 * @date 2015-8-30 下午5:52:44
 */
public class ExchBuyEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int uid 	= data.readInt();
		int count 	= data.readInt();
		
		ErrorCode code = null;
		List<IProp> ret = null;
		try {
			
			HomePlanet home = WorldManager.o.getHPlanetInPlayer(player);
			// 获取交易所道具
			ExchGoods goods = home.getExchange().getGoods( uid );
			if( goods == null )
				throw new Exception( ErrorCode.PROP_NOTEXIST.name() );
			if( goods.getCount() < count )
				throw new Exception( ErrorCode.PROP_LAZYWEIGHT.name() );
			
			// 检测钱是否足够
			if( player.changeCurrency( -(goods.getUnitprice()*count) ) == -1 )
				throw new Exception( ErrorCode.CURRENCY_LAZYWEIGHT.name() );
			
			// 在交易所扣除对应道具
			if( goods.deduct(count) )
				home.getExchange().removeGoods( goods );

			// 放入玩家仓库
			IProp clone = goods.getProp().clone();
			clone.setCount(count);
			ret = player.getDepots().appendProp( clone );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 20 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeByte( ret.size() );
			for( IProp prop : ret ){
				prop.putBaseBuffer(response);
			}
		}
		sendPackage( player.getCtx(), response );
		
	}

}
