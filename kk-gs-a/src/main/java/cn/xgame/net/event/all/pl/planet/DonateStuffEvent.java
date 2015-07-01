package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.a.world.WorldManager;
import cn.xgame.net.event.IEvent;

/**
 * 捐献资源 - 材料
 * @author deng		
 * @date 2015-7-1 上午10:24:48
 */
public class DonateStuffEvent extends IEvent{

	@Override
	public void run( Player player, ByteBuf data ) throws IOException {
		
		short id	= data.readShort();
		PropType type = PropType.fromNumber( data.readByte() );
		int uid 	= data.readInt();
		int count	= data.readInt();
		
		ErrorCode code = null;
		
		try {
			// 判断是否有足够的材料
			IProp prop = player.getProps().getProp( type, uid );
			if( prop == null )
				throw new Exception( ErrorCode.STUFF_NOTEXIST.name() );
			if( prop.getCount() < count )
				throw new Exception( ErrorCode.STUFF_LAZYWEIGHT.name() );
			
			// 找到对应星球捐献资源
			WorldManager.o.donateResource( player, id, prop );
			
			// 扣除 玩家身上的道具
			player.getProps().remove(prop);
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
	}

}
