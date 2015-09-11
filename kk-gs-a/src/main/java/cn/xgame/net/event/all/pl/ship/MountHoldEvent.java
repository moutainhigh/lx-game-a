package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.dock.ship.o.IHold;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;

/**
 * 放入道具到舰船货仓
 * @author deng		
 * @date 2015-7-23 上午11:22:59
 */
public class MountHoldEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int suid 	= data.readInt();//舰船唯一ID
		int puid 	= data.readInt();//道具唯一ID
		int count 	= data.readInt();//道具个数
		
		ErrorCode code 		= null;
		List<IProp> ret 	= null;
		try {
			ShipInfo ship 	= player.getDocks().getShipOfException(suid);
			ship.isHaveCaptain();
			// 获取道具
			StarDepot depot = player.getDepots(ship.getBerthSid());
			IProp prop 		= depot.getPropOfException(puid);
			
			// 判断道具个数是否足够
			if( prop.getCount() < count )
				throw new Exception( ErrorCode.PROP_LAZYWEIGHT.name() ) ;
			
			// 先拷贝一个出来
			IProp clone = prop.clone();
			clone.setCount(count);

			IHold holds = ship.getHolds();
			// 看货仓是否 还有空间
			if( !holds.roomIsEnough( clone ) )
				throw new Exception( ErrorCode.ROOM_LAZYWEIGHT.name() );
			// 放入货仓
			ret = holds.appendProp( clone );
			// 最后保存数据库
			ship.updateDB( player );
			
			// 成功后 就把道具从玩家仓库扣除相应道具
			depot.deductProp( puid, count );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 50 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeInt( suid );
			response.writeInt( puid );
			response.writeInt( count );
			response.writeByte( ret.size() );
			for( IProp prop : ret ){
				prop.putBaseBuffer2(response);
			}
		}
		sendPackage( player.getCtx(), response );
		
	}

}
