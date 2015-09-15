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

/**
 * 商店 购买 
 * @author deng		
 * @date 2015-7-20 上午11:06:58
 */
public class ShopBuyEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int id 		= data.readInt(); // 星球ID
		int nid 	= data.readInt(); // 物品ID
		int count 	= data.readInt(); // 购买数量
		
		ErrorCode code = null;
		List<IProp> ret = null;
		try {
			
			HomePlanet planet = WorldManager.o.getHomePlanet( id );
			
			// 获取道具
			IProp prop = planet.getShopProp( nid );
			
			// 数量是否足够 只有特产 才做这个判断
			if( prop.getCount() < count && prop.getUid() == 1 )
				throw new Exception( ErrorCode.PROP_LAZYWEIGHT.name() );
			
			// 算出货币
			int needGold = prop.getSellgold() * count;
			//先判断 玩家是否该星球的
			if( planet.getChild( player.getUID() ) == null )
				needGold += 100;
			
			// 看金币是否足够
			if( player.changeCurrency( -needGold ) == -1 )
				throw new Exception( ErrorCode.CURRENCY_LAZYWEIGHT.name() );
			
			// 加入玩家背包
			ret = player.getDepots( id ).appendProp( nid, count );
			
			// 如果是特产 那么就要对应扣除数量
			if( prop.getUid() == 1 ){
				planet.getSpecialtyControl().deduct( nid, count );
				// 同步给其他玩家
				// TODO
			}
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeInt( player.getCurrency() );
			response.writeInt( id );
			response.writeInt( count );
			response.writeByte( ret.size() );
			for( IProp prop : ret ){
				prop.putBaseBuffer(response);
				prop.buildTransformStream(response);
			}
		}
		sendPackage( player.getCtx(), response );
		
	}

}
