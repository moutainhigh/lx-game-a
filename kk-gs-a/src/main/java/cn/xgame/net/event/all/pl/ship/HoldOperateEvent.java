package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.dock.DockControl;
import cn.xgame.a.player.dock.classes.IHold;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;

/**
 * 放入and取下 道具到舰船货仓
 * @author deng		
 * @date 2015-7-23 上午11:22:59
 */
public class HoldOperateEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int suid 	= data.readInt();//舰船唯一ID
		int uid		= data.readInt();
		int count 	= data.readInt();
		
		ErrorCode code 		= null;
		List<IProp> ret 	= Lists.newArrayList();
		try {
			DockControl docks = player.getDocks();
			ShipInfo ship 	= docks.getShipOfException(suid);
			if( !docks.isLeisure( ship ) )
				throw new Exception( ErrorCode.SHIP_NOTLEISURE.name() );
			
			StarDepot depot = player.getDepots(ship.getBerthSid());
			IHold holds 	= ship.getHolds();
			
			// 个数大于0表示 放入货仓 小于0表示取出货仓
			if( count >= 0 ){
				// 获取道具
				IProp prop 		= depot.getPropOfException(uid);
				
				// 判断道具个数是否足够
				if( prop.getCount() < count )
					throw new Exception( ErrorCode.PROP_LAZYWEIGHT.name() ) ;
				
				// 先拷贝一个出来
				IProp clone = prop.clone();
				clone.setCount(count);
				
				// 看货仓是否 还有空间
				if( !holds.roomIsEnough( clone ) )
					throw new Exception( ErrorCode.ROOM_LAZYWEIGHT.name() );
				
				// 这里先扣除 因为下面放入货仓的时候会改变UID
				depot.deductProp( prop );
				
				// 放入货仓
				ret.addAll( holds.appendProp( prop ) );
			}else{
				// 执行扣除
				IProp prop 		= ship.getHolds().deductProp( uid, count );
				// 成功后 就把道具放入玩家仓库
				if( prop != null )
					ret.addAll( depot.appendProp( prop ) );
			}
			
			// 最后保存数据库
			ship.updateDB( player );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 50 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeInt( suid );
			response.writeInt( uid );
			response.writeInt( count );
			response.writeByte( ret.size() );
			for( IProp prop : ret ){
				prop.putBaseBuffer(response);
				prop.buildTransformStream(response);
			}
		}
		sendPackage( player.getCtx(), response );
		
	}

}

class Props{
	int id;
	int count;
}
