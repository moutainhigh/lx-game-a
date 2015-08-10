package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;

/**
 * 申请某个母星球数据
 * @author deng		
 * @date 2015-6-30 上午10:10:37
 */
public class ApplyHomeEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int nid = data.readInt();
		
		HomePlanet home = WorldManager.o.getHomePlanet( nid );
		ErrorCode code = null;
		try {
			if( home == null )
				throw new Exception( ErrorCode.PLANET_NOTEXIST.name() );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 512 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			home.buildTransformStream( response );
			home.putPlyaerInfo(player, response);
		}
		sendPackage( player.getCtx(), response );
		
	}

}
