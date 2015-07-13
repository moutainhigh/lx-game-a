package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.net.event.IEvent;

/**
 * 申请星球资源数据
 * @author deng		
 * @date 2015-6-30 上午10:12:18
 */
public class ApplyResEvent extends IEvent{

	@Override
	public void run( Player player, ByteBuf data ) throws IOException {
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
			planet.getDepotControl().buildTransformStream(response);
		}
		sendPackage( player.getCtx(), response );
		
	}

}
