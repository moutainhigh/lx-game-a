package cn.xgame.net.event.all.pl.transaction;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;

/**
 * 交易所-收款
 * @author deng		
 * @date 2015-10-8 下午3:52:38
 */
public class ExchCollectEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		ErrorCode code = null;
		int collect = 0;
		try {
			
			HomePlanet home = WorldManager.o.getHPlanetInPlayer(player);
			
			collect = home.getExchange().receipt( player );
			
			// 加入到玩家身上
			player.changeCurrency( collect );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf respon = buildEmptyPackage( player.getCtx(), 10 );
		respon.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			respon.writeInt( collect );
			respon.writeInt( player.getCurrency() );
		}
		sendPackage( player.getCtx(), respon );
	}

}
