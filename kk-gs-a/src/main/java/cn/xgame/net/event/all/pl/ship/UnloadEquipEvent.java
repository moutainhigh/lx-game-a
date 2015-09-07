package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.ship.o.ShipInfo;
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
		
		int suid = data.readInt();
		int puid = data.readInt();
		
		ErrorCode code = null;
		IProp ret = null;
		try {
			ShipInfo ship = player.getDocks().getShipOfException(suid);
			// 检查是否在母星 只有在母星才能操作
			if( ship.getBerthSnid() != player.getCountryId() )
				throw new Exception( ErrorCode.NOT_ATSAMESTAR.name() ) ;
			// 获取道具
			IProp prop = ship.getEquips().getPropOfException( puid );
			
			// 拷贝一份
			ret = prop.clone();
			
			// 放入玩家背包
			player.getDepots(ship.getBerthSnid()).appendProp(prop);
			
			// 然后从舰船装备里面删除
			player.getDocks().removeEquipAtShip( ship, prop );
			
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
