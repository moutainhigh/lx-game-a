package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.manor.classes.IBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 销毁建筑
 * @author deng		
 * @date 2015-10-15 下午4:21:12
 */
public class DestroyBuildingEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte index = data.readByte();
		
		ErrorCode code = null;
		IBuilding building = null;
		try {
			// 获取建筑 并 判断是否可以销毁
			building = player.getManors().getBuildByIndex( index );
			if( building == null || !building.isDestroy() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 直接开始销毁
			building.inDestroy();
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 11 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( index );
			buffer.writeInt( building.getEndtime() );
			buffer.writeInt( player.getCurrency() );
		}
		sendPackage( player.getCtx(), buffer );
		
	}

}
