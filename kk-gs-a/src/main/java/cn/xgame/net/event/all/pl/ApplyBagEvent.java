package cn.xgame.net.event.all.pl;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.player.captain.o.CaptainInfo;
import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.cequip.CEquipAttr;
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
		size += putBagData( response, bag, 0, -1 );
		// 玩家舰船
		List<ShipInfo> ships = player.getDocks().getAll();
		for( ShipInfo ship : ships ){
			// 装备
			bag = ship.getEquips().getAll();
			size += putBagData( response, bag, 1, ship.getuId() );
			// 货仓
			bag = ship.getHolds().getAll();
			size += putBagData( response, bag, 2, ship.getuId() );
		}
		// 玩家舰长
		List<CaptainInfo> captains = player.getCaptains().getAll();
		for( CaptainInfo captain : captains ){
			CEquipAttr equip = captain.getEquips().getEquip();
			if( equip == null ) continue;
			equip.putBaseBuffer(response);
			response.writeByte( 3 );
			response.writeInt( captain.getuId() );
			equip.buildTransformStream(response);
			++size;
		}
		
		response.setShort( 5, size );
		sendPackage( player.getCtx(), response );
	}

	private int putBagData( ByteBuf response, List<IProp> bag, int type, int theirId ) {
		for( IProp prop : bag ){
			prop.putBaseBuffer(response);
			response.writeByte( type );
			response.writeInt( theirId );
			prop.buildTransformStream(response);
		}
		return bag.size();
	}

}
