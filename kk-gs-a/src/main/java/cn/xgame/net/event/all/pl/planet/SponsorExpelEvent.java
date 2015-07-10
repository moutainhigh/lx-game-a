package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;

/**
 * 发起驱逐元老
 * @author deng		
 * @date 2015-7-1 上午10:30:26
 */
public class SponsorExpelEvent extends IEvent{

	@Override
	public void run( Player player, ByteBuf data ) throws IOException {
		
		String uid 		= RW.readString(data);
		String explain 	= RW.readString(data);
		
		ErrorCode code = null;
		try {
			
			// 获取玩家 母星 - 这里暂时 默认在母星发起投票
			IPlanet planet = WorldManager.o.getHPlanetInPlayer(player);
			
			// 开始发起 投票
			planet.sponsorGenrVote( player, uid, explain );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
		
	}

}
