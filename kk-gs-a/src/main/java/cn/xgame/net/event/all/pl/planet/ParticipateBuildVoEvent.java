package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.net.event.IEvent;

/**
 * 参与 建筑 投票
 * @author deng		
 * @date 2015-7-1 上午10:31:37
 */
public class ParticipateBuildVoEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int nid 		= data.readShort();
		byte isAgree 	= data.readByte();
		
		ErrorCode code = null;
		
		try {
			// 获取对应星球  - 这里暂时 默认在母星发起投票
			IPlanet planet = WorldManager.o.getHPlanetInPlayer(player);
			
			// 参与投票
			planet.participateBuildVote( player, nid, isAgree );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
	}

}
