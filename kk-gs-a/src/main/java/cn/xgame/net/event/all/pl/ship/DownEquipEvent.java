package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;

/**
 * 取下 一个装备
 * @author deng		
 * @date 2015-7-28 上午10:56:19
 */
public class DownEquipEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int suid = data.readInt();
		int puid = data.readInt();
		
		ErrorCode code = null;
		IProp ret = null;
		try {
			// 获取道具
			IProp prop = player.getDocks().getEquipAtShip( suid, puid );
			if( prop == null )
				throw new Exception( ErrorCode.PROP_NOTEXIST.name() ) ;
			
			// 拷贝一份
			ret = prop.clone();
			
			// 放入玩家背包
			player.getDepots().append(ret);
			
			// 然后从舰船装备里面删除
			player.getDocks().removeEquipAtShip( suid, prop );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 6 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( ret.getuId() );
		}
		sendPackage( player.getCtx(), buffer );
	}

}
