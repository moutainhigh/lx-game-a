package cn.xgame.net.event.all.pl.transaction;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.dock.capt.CaptainInfo;
import cn.xgame.a.player.tavern.TavernCaptain;
import cn.xgame.a.player.tavern.TavernData;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ItemPo;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;

/**
 * 酒馆 购买
 * @author deng		
 * @date 2015-7-20 上午11:12:12
 */
public class TavernBuyEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int id 	= data.readInt();
		int nid = data.readInt();
	
		ErrorCode code 	= null;
		CaptainInfo ret = null;
		try {
			
			HomePlanet planet = WorldManager.o.getHomePlanet( id );
			
			// 获取舰长
			TavernData tavernData = player.getTaverns().get( id );
			if( tavernData == null )
				throw new Exception( ErrorCode.PROP_NOTEXIST.name() );
			TavernCaptain captain = tavernData.getCaptain( nid );
			if( captain == null )
				throw new Exception( ErrorCode.PROP_NOTEXIST.name() );
			
			// 算出货币
			ItemPo item 	= CsvGen.getItemPo( nid );
			int needGold 	= item.buygold <= 0 ? 1 : item.buygold;
			//先判断 玩家是否该星球的
			if( planet.getChild( player.getUID() ) == null )
				needGold += 1000;
			
			// 看金币是否足够
			if( player.changeCurrency( -needGold ) == -1 )
				throw new Exception( ErrorCode.CURRENCY_LAZYWEIGHT.name() );
			
			// 放入舰长室
			ret 			= player.getDocks().createCaptain( id, captain.id, captain.quality );
			
			// 将舰长从酒馆里面删除掉
			tavernData.remove( nid );
			
			Logs.debug( player, "在酒馆购买舰长 nid=" + ret.getnId() + ", uid=" + ret.getuId() );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
	
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeInt( player.getCurrency() );
			// 舰长属性
			ret.buildTransformStream(response);
		}
		sendPackage( player.getCtx(), response );
		
	}

}
