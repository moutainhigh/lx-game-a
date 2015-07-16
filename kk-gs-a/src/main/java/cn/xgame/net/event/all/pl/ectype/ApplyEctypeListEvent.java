package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;


import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.data.ectype.IEctype;
import cn.xgame.a.world.planet.data.ectype.o.EctypeLists;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 申请母星所有副本列表
 * @author deng		
 * @date 2015-7-13 下午5:50:18
 */
public class ApplyEctypeListEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int suid = data.readInt();
		
		// 获取母星
		HomePlanet planet = WorldManager.o.getHPlanetInPlayer(player);
		if( planet == null ) {
			Logs.error( player, "planet == null at ApplyEctypeListEvent.run" );
			return;
		}
		
		// 获取舰船
		ShipInfo ship = player.getDocks().getShip( suid );
		if( ship == null ) {
			Logs.error( player, "ship == null at ApplyEctypeListEvent.run" );
			return;
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		
		// 获取所有常规副本
		List<EctypeLists> ectypes = planet.getEctypeControl().getGeneralEctype();
		response.writeByte( ectypes.size() );
		for( EctypeLists ectype : ectypes ){
			response.writeInt( ectype.getStarId() );
			List<IEctype> v = ectype.getEctypes();
			response.writeByte( v.size() );
			// 这里判断 如果有队伍 那么要计算出  最大航行时间和战斗时间
			// TODO
			for( IEctype o : v ){
				response.writeInt( o.getNid() );
				response.writeInt( ship.getSailingTime( ectype.getStarId() ) );
				response.writeInt( 10 );
			}
		}
		
		// 获取所有非常规副本
		ectypes = planet.getEctypeControl().getNotGeneralEctype();
		response.writeByte( ectypes.size() );
		for( EctypeLists ectype : ectypes ){
			RW.writeString( response, player.getUID() );
			IEctype o = ectype.getEctypes().get(0);
			response.writeInt( o.getNid() );
			response.writeInt( 10 );
			response.writeInt( -1 );
			response.writeInt( 100 );
			response.writeInt( 10 );
		}
		sendPackage( player.getCtx(), response );
	}

}
