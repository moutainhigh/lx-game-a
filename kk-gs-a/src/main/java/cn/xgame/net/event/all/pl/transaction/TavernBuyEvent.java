package cn.xgame.net.event.all.pl.transaction;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;

/**
 * 酒馆 
 * @author deng		
 * @date 2015-7-20 上午11:12:12
 */
public class TavernBuyEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int id 	= data.readInt();
		int nid = data.readInt();
	
		ErrorCode code = null;
		IProp ret = null;
		try {
			
			HomePlanet planet = WorldManager.o.getHomePlanet(id);
			if( planet == null )
				throw new Exception( ErrorCode.PLANET_NOTEXIST.name() );
			
			// 执行
			ret = planet.runTavernBuy( player, nid );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
	
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeInt( ret.getuId() );
		}
		sendPackage( player.getCtx(), response );
		
	}

}
