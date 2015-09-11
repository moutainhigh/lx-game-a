package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;

/**
 * 卸下 货仓物品
 * @author deng		
 * @date 2015-7-23 上午11:24:05
 */
public class UnloadHoldEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int suid 	= data.readInt();//舰船唯一ID
		int puid 	= data.readInt();//道具唯一ID
		int count 	= data.readInt();//道具个数
		
		ErrorCode code 	= null;
		List<IProp> ret = null;
		try {
			ShipInfo ship 	= player.getDocks().getShipOfException(suid);
			ship.isHaveCaptain();
			
			// 执行扣除
			IProp prop 		= ship.getHolds().deductProp( puid, count );
			if( prop == null )
				throw new Exception( ErrorCode.PROP_NOTEXIST.name() ) ;
			// 保存一下数据库
			ship.updateDB( player );
			
			// 成功后 就把道具放入玩家仓库
			ret 			= player.getDepots(ship.getBerthSid()).appendProp( prop );
			
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
