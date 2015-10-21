package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.classes.BuildingType;
import cn.xgame.a.player.manor.classes.IBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.system.LXConstants;

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
			ManorControl manors = player.getManors();
			manors.update();
			
			// 获取建筑 并 判断是否可以销毁
			building = manors.getBuildByIndex( index );
			if( building == null || !building.isDestroy() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			if( building.getType() != BuildingType.INSERVICE )
				throw new Exception( ErrorCode.MANOR_TIME_ISYET.name() );
			
			// 直接开始销毁
			building.inDestroy();
			
			// 把钱给玩家
			int money 	= getMoney( building.templet().needres );
			money		= (int) (money * 0.2);
			player.changeCurrency( money, "领地建筑拆毁获得" );
			
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
		if( code == ErrorCode.MANOR_TIME_ISYET ){
			buffer.writeInt( building.getEndtime() );
		}
		sendPackage( player.getCtx(), buffer );
	}

	// 从这个字符串里面获取金币
	private int getMoney( String needres ) {
		String[] content = needres.split( "\\|" );
		for( String str : content ){
			String[] o 	= str.split( ";" );
			int id 		= Integer.parseInt( o[0] );
			int count	= Integer.parseInt( o[1] );
			if( id == LXConstants.CURRENCY_NID )
				return count;
		}
		return 0;
	}

}
