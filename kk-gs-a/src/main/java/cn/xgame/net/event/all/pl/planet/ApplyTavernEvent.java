package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.tavern.TavernData;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请母星酒馆信息
 * @author deng		
 * @date 2015-7-16 上午9:45:32
 */
public class ApplyTavernEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int snid = player.getCountryId();
		
		ErrorCode code = null;
		TavernData tavernData = null;
		try {
			
			// 获取玩家 母星 
			tavernData = player.getTaverns().get( snid );
			if( tavernData == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 这里更新一下酒馆
			if( tavernData.isUpdate() ){
				tavernData.updateCaptain();
			}
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			tavernData.buildTransformStream(response);
		}
		sendPackage( player.getCtx(), response );
	}

}
