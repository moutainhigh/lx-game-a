package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.dock.ship.o.IHold;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.sequip.SEquipAttr;
import cn.xgame.net.event.IEvent;

/**
 * 装上一个 装备
 * @author deng		
 * @date 2015-7-28 上午10:53:52
 */
public class MountEquipEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int suid 	= data.readInt(); // 舰船UID
		int puid	= data.readInt(); // 道具UID
		
		ErrorCode code 	= null;
		IProp ret 		= null;
		try {
			ShipInfo ship 	= player.getDocks().getShipOfException(suid);

			// 检测是否空闲状态
			player.getDocks().isLeisure( ship );
			
			// 获取道具
			StarDepot depot = player.getDepots( ship.getBerthSid() );
			IProp prop		= depot.getPropOfException(puid);
			
			// 先拷贝一个出来
			IProp clone 	= prop.clone();
			if( !clone.isShipEquip() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			SEquipAttr equip 	= (SEquipAttr)clone;
			IHold hold 			= equip.isWeapon() ? ship.getWeapons() : ship.getAssists();
			
			// 看货仓是否 还有空间
			if( !hold.roomIsEnough( clone ) )
				throw new Exception( ErrorCode.ROOM_LAZYWEIGHT.name() );
			// 看能量是否足够
			int needEnergy 		= equip.getEnergy() + ship.allOccupyEnergy();
			if( needEnergy > ship.attr().getMaxEnergy() )
				throw new Exception( ErrorCode.ENERGY_LAZYWEIGHT.name() );
			
			// 开始放入舰船货仓
			ret = hold.put(clone);
			ship.updateDB( player );
			
			// 从玩家仓库中删除掉
			depot.remove( prop );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 6 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( suid );
			buffer.writeInt( puid );
			buffer.writeInt( ret.getUid() );
		}
		sendPackage( player.getCtx(), buffer );
		
	}

}
