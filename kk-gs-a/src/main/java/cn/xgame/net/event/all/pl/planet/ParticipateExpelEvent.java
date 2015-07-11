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
 * 参与驱逐元老投票
 * @author deng		
 * @date 2015-7-2 上午11:04:04
 */
public class ParticipateExpelEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		String uid 		= RW.readString(data);
		byte isAgree 	= data.readByte();
		
		ErrorCode code = null;
		
		try {
			// 获取对应星球  - 这里暂时 默认在母星发起投票
			IPlanet planet = WorldManager.o.getHPlanetInPlayer(player);
			
			// 参与投票
			planet.participateGenrVote(player, uid, isAgree);
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
	}

}
