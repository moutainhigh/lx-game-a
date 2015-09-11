package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;

/**
 * 取下 一个装备
 * @author deng		
 * @date 2015-7-28 上午10:56:19
 */
public class UnloadEquipEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int suid = data.readInt();// 舰船UID
		int puid = data.readInt();// 道具UID
		
		ErrorCode code 	= null;
		IProp ret 		= null;
		try {
			ShipInfo ship = player.getDocks().getShipOfException(suid);
			ship.isHaveCaptain();
			// 检测是否空闲状态
			player.getDocks().isLeisure( ship );
			
			// 在舰船里面删除道具
			ret 		= ship.removeEquip( puid );
			if( ret == null )
				throw new Exception( ErrorCode.SHIP_NOTEXIST.name() );
			ship.updateDB( player );
			
			// 放入玩家背包
			player.getDepots(ship.getBerthSid()).appendProp( ret );
			
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
