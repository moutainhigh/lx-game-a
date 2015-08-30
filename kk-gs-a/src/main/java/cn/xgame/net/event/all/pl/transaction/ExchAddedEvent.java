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
 * 交易所-上架
 * @author deng		
 * @date 2015-8-30 下午4:16:28
 */
public class ExchAddedEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int uid 	= data.readInt();
		int count 	= data.readInt();
		int price 	= data.readInt();
		
		ErrorCode code 	= null;
		int retUid 		= 0;
		try {
			
			// 仓库是否有这个道具
			IProp prop = player.getDepots().getProp(uid);
			if( prop == null )
				throw new Exception( ErrorCode.PROP_NOTEXIST.name() );
			// 数量是否够
			if( prop.getCount() < count )
				throw new Exception( ErrorCode.PROP_LAZYWEIGHT.name() );
			
			// 拷贝一个出来 上架
			IProp newPorp 	= prop.clone();
			HomePlanet home = WorldManager.o.getHPlanetInPlayer(player);
			retUid 			= home.getExchange().added( player, newPorp, count, price );
			
			// 最后在玩家仓库扣除对应道具
			player.getDepots().deductProp( prop.getUid(), count );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 20 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeInt(retUid);
		}
		sendPackage( player.getCtx(), response );
		
	}

}
