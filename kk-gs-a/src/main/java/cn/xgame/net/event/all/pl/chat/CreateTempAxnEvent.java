package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.o.ChatType;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.event.all.pl.update.Update_3010;
import cn.xgame.net.netty.Netty.RW;

/**
 * 创建一个临时频道
 * @author deng		
 * @date 2015-7-25 上午10:42:07
 */
public class CreateTempAxnEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		String ruid = RW.readString(data);
		
		ErrorCode code 	= null;
		Player to 		= null;
		int axnId 		= 0;
		try {
			
			// 判断自己的临时频道是否已经满了
			if( player.getChats().axnIsMax( ChatType.TEMPAXN ) )
				throw new Exception( ErrorCode.AXN_ISMAX.name() );
			
			to	 = PlayerManager.o.getPlayerFmOnline(ruid);
			if( to == null )
				throw new Exception( ErrorCode.PLAYER_NOTEXIST.name() );
			// 判断被邀请者频道是否已经满了
			if( to.getChats().axnIsMax( ChatType.TEMPAXN ) )
				throw new Exception( ErrorCode.AXN_ISMAX.name() );
			
			// 生成频道唯一ID
			axnId = ChatType.TEMPAXN.generateUID();
			// 创建一个频道
			ChatManager.o.getChatControl().createAxn( ChatType.TEMPAXN, axnId, player, to );
			
			// 记录到玩家身上
			player.getChats().appendAxn( ChatType.TEMPAXN, axnId );
			to.getChats().appendAxn( ChatType.TEMPAXN, axnId );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 125 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeInt(axnId);
		}
		sendPackage( player.getCtx(), response );
		
		// 同步消息
		if( code == ErrorCode.SUCCEED && to != null ){
			((Update_3010)Events.UPDATE_3010.getEventInstance()).run( to, axnId );
		}
		
	}
	
}
