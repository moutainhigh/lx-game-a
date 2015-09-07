package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.depot.PlayerDepot;
import cn.xgame.a.player.ship.o.ShipInfo;
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
		
		int suid = data.readInt();
		int puid = data.readInt();
		int count = data.readInt();
		
		ErrorCode code = null;
		List<IProp> ret = null;
		try {
			ShipInfo ship = player.getDocks().getShipOfException(suid);
			// 检查是否在母星 只有在母星才能操作
			if( ship.getBerthSnid() != player.getCountryId() )
				throw new Exception( ErrorCode.NOT_ATSAMESTAR.name() ) ;
			// 检查道具是否存在
			PlayerDepot depot = player.getDepots(ship.getBerthSnid());
			IProp prop = depot.getPropOfException(puid);
			// 判断道具个数是否足够
			if( prop.getCount() < count )
				throw new Exception( ErrorCode.PROP_LAZYWEIGHT.name() ) ;
			
			// 先拷贝一个出来
			IProp clone = prop.clone();
			clone.setCount(count);
			
			// 开始放入舰船货仓
			ret = player.getDocks().putinHold( ship, clone );
			
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
				prop.putBaseBuffer(response);
			}
		}
		sendPackage( player.getCtx(), response );
		
	}

}
