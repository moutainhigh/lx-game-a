package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.net.event.IEvent;

/**
 * 申请所有政务列表
 * @author deng		
 * @date 2015-7-7 下午4:06:10
 */
public class ApplyAlllAffairEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int nid = data.readInt();
		
		IPlanet planet = WorldManager.o.getPlanet( nid );
		ErrorCode code = null;
		try {
			if( planet == null )
				throw new Exception( ErrorCode.PLANET_NOTEXIST.name() );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 512 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			planet.putAlllAffair(response);
		}
		sendPackage( player.getCtx(), response );
	}

}
