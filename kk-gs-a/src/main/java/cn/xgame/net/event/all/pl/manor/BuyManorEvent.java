package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ReclaimcapacityPo;
import cn.xgame.net.event.IEvent;

/**
 * 购买领地
 * @author deng		
 * @date 2015-9-25 上午10:58:18
 */
public class BuyManorEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int snid = data.readInt();
		int mnid = data.readInt();
		
		ErrorCode code = null;
		try {
			ManorControl manors = player.getManors();
			
			// 判断是否比当前领地等级大
			if( mnid <= manors.getNid() )
				throw new Exception( ErrorCode.MANOR_LEVEL_TOOLOW.name() );

			// 获取领地
			ReclaimcapacityPo reclaim = CsvGen.getReclaimcapacityPo(mnid); 
			if( reclaim == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 判断科技等级是否达到
			HomePlanet home = WorldManager.o.getHomePlanet(snid);
			if( home.getTechLevel() < reclaim.techlv )
				throw new Exception( ErrorCode.CON_DISSATISFY.name() );
			
			// 执行扣钱 
			if( player.changeCurrency( -reclaim.money ) == -1 )
				throw new Exception( ErrorCode.CURRENCY_LAZYWEIGHT.name() );
			
			// 直接设置领地即可
			manors.setTerritory(reclaim);
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buff = buildEmptyPackage( player.getCtx(), 10 );
		buff.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buff.writeInt( player.getCurrency() );
			buff.writeInt( mnid );
		}
		sendPackage( player.getCtx(), buff );
		
	}

}
