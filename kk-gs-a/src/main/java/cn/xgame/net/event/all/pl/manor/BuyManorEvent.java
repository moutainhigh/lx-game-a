package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.classes.BType;
import cn.xgame.a.player.manor.info.BaseBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.BbuildingPo;
import cn.xgame.config.o.ReclaimPo;
import cn.xgame.net.event.IEvent;
import cn.xgame.system.LXConstants;
import cn.xgame.utils.Logs;

/**
 * 购买领地
 * @author deng		
 * @date 2015-9-25 上午10:58:18
 */
public class BuyManorEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int mnid = data.readInt();
		
		ManorControl manors = player.getManors();
		ErrorCode code = null;
		try {
			
			// 判断是否比当前领地等级大
			if( mnid <= manors.getNid() )
				throw new Exception( ErrorCode.MANOR_LEVEL_TOOLOW.name() );

			// 获取领地
			ReclaimPo reclaim = CsvGen.getReclaimPo(mnid); 
			if( reclaim == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 判断科技等级是否达到
			HomePlanet home = WorldManager.o.getHPlanetInPlayer(player);
			if( home.getTechLevel() < reclaim.techlv )
				throw new Exception( ErrorCode.CON_DISSATISFY.name() );
			
			// 执行扣钱 
			if( player.changeCurrency( -reclaim.money, "购买领地" ) == -1 )
				throw new Exception( ErrorCode.CURRENCY_LAZYWEIGHT.name() );
			
			// 这里看之前是否有过领地 如果是第一次购买那么默认就要添加一个基地建筑
			if( manors.getTerritory() == null ){
				createBaseBuilding(manors);
			}
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
	
	/**
	 * 创建一个基地建筑
	 * @param manors
	 */
	private void createBaseBuilding(ManorControl manors) {
		BbuildingPo templet = CsvGen.getBbuildingPo( LXConstants.BASE_BUILD_ID );
		if( templet != null ){
			BaseBuilding building = new BaseBuilding(BType.BASE, templet);
			building.build();
			manors.addBuilding(building);
		}else{
			Logs.error( "玩家第一次购买领地 创建基地建筑失败 at="+LXConstants.BASE_BUILD_ID+" 在表格没有找到" );
		}
	}

}
