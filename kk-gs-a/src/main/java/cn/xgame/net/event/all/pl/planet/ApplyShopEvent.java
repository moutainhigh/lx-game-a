package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;

/**
 * 申请星球商店数据
 * @author deng		
 * @date 2015-6-30 下午4:47:08
 */
public class ApplyShopEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int nid = data.readInt();
		
		HomePlanet planet = WorldManager.o.getHomePlanet(nid);
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
			List<IProp> ls = planet.getShopList();
			response.writeByte( ls.size() );
			for( IProp prop : ls ){
				response.writeInt( prop.getNid() );
				response.writeInt( prop.getCount() );
			}
		}
		sendPackage( player.getCtx(), response );
	}

}
