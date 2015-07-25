package cn.xgame.net.event.all.pl;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.player.captain.o.CaptainInfo;
import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;

/**
 * 申请背包基础数据
 * @author deng		
 * @date 2015-7-8 上午11:09:56
 */
public class ApplyBagEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		
		short size = 0;
		response.writeShort( size );
		
		// 玩家背包
		List<IProp> bag = player.getDepots().getAll();
		size += bag.size();
		putBuffer( response, bag, 0, 0, 0 );
		
		// 船坞
		List<ShipInfo> ships = player.getDocks().getAll();
		for( ShipInfo ship : ships ){
			// 货仓
			bag = ship.getHolds().getAll();
			size += bag.size();
			putBuffer( response, bag , ship.getuId(), ship.getnId() , 2 );
			// 武器
			bag = ship.getWeapons().getAll();
			size += bag.size();
			putBuffer( response, bag , ship.getuId(), ship.getnId() , 1 );
			// 辅助
			bag = ship.getAssists().getAll();
			size += bag.size();
			putBuffer( response, bag , ship.getuId(), ship.getnId() , 1 );
		}
		
		// 舰长
		List<CaptainInfo> captains = player.getCaptains().getAll();
		for( CaptainInfo captain : captains ){
			bag = captain.getEquips().getAll();
			size += bag.size();
			putBuffer( response, bag , captain.getuId(), captain.getnId() , 1 );
		}
		
		// 设置背包长度
		response.setShort( 5, size );
		
		sendPackage( player.getCtx(), response );
	}

	private void putBuffer( ByteBuf response, List<IProp> bag, int theirUid, int theirNid, int type ) {
		for( IProp prop : bag ){
			prop.putBaseBuffer(response);
			response.writeInt( theirUid );
			response.writeInt( theirNid );
			response.writeByte( type );
		}
	}

}
