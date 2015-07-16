package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;

/**
 * 申请母星酒馆信息
 * @author deng		
 * @date 2015-7-16 上午9:45:32
 */
public class ApplyTavernEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		HomePlanet planet = null;
		ErrorCode code = null;
		try {
			
			// 获取玩家 母星 - 这里暂时 默认在母星发起投票
			planet = WorldManager.o.getHPlanetInPlayer(player);
			if( planet == null )
				throw new Exception( ErrorCode.PLANET_NOTEXIST.name() );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			planet.getTavernControl().buildTransformStream(response);
		}
		sendPackage( player.getCtx(), response );
	}

}
