package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;

/**
 * 装上一个 装备
 * @author deng		
 * @date 2015-7-28 上午10:53:52
 */
public class MountEquipEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		int suid 	= data.readInt();
		int atsuid 	= data.readInt();
		int puid	= data.readInt();
		
		ErrorCode code = null;
		IProp ret = null;
		try {
			if( suid == atsuid )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			ShipInfo ship 	= player.getDocks().getShipOfException(suid);
			if( !ship.isLevitation() )
				throw new Exception( ErrorCode.SHIP_NOTLEISURE.name() );
			// 获取道具
			IProp prop 		= null;
			ShipInfo atship = null;
			if( atsuid == -1 ){
				prop		= getPlayerProp( player, puid );
			}else{
				atship 		= player.getDocks().getShipOfException(atsuid);
				if( !atship.isLevitation() )
					throw new Exception( ErrorCode.SHIP_NOTLEISURE.name() );
				prop		= getShipProp( player, ship, atship, puid );
			}
			
			// 先拷贝一个出来
			IProp clone = prop.clone();
			
			// 开始放入舰船货仓
			ret 		= player.getDocks().mountEquip( ship, clone );
			
			// 成功后 直接删除道具
			if( atsuid == -1 ){
				player.getDepots().remove( prop );
			}else{
				player.getDocks().removeEquipAtShip( atship, prop );
			}
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 6 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( suid );
			buffer.writeInt( puid );
			buffer.writeInt( ret.getuId() );
		}
		sendPackage( player.getCtx(), buffer );
		
	}

	// 从舰船上面获取道具
	private IProp getShipProp(Player player, ShipInfo ship, ShipInfo atship, int puid) throws Exception {
		// 判断两个舰船是否在一个星球
		if( ship.getBerthSnid() != atship.getBerthSnid() )
			throw new Exception( ErrorCode.NOT_ATSAMESTAR.name() ) ;
		// 取出装备
		return atship.getEquips().getPropOfException(puid);
	}

	// 从玩家身上获取道具
	private IProp getPlayerProp(Player player, int puid) throws Exception {
		IProp prop = player.getDepots().getPropOfException(puid);
		// 判断是否舰船装备
		if( !prop.isShipEquip() )
			throw new Exception( ErrorCode.NOT_SHIPEQUIP.name() ) ;
		return prop;
	}

}
