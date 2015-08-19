package cn.xgame.net.event.all.pl.transaction;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;

/**
 * 商店 购买 
 * @author deng		
 * @date 2015-7-20 上午11:06:58
 */
public class ShopBuyEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int id 		= data.readInt();
		int nid 	= data.readInt();
		int count 	= data.readInt();
		
		ErrorCode code = null;
		List<IProp> ret = null;
		try {
			HomePlanet planet = WorldManager.o.getHomePlanet(id);
			if( planet == null )
				throw new Exception( ErrorCode.PLANET_NOTEXIST.name() );
			
			ret = planet.runShopBuy( player, nid, count );
			
			Logs.debug( player, "在商店购买道具 nid=" + nid );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeByte( ret.size() );
			for( IProp prop : ret ){
				response.writeInt( prop.getUid() );
				response.writeInt( prop.getNid() );
				response.writeInt( prop.getCount() );
			}
			response.writeInt( player.getCurrency() );
		}
		sendPackage( player.getCtx(), response );
		
	}

}
