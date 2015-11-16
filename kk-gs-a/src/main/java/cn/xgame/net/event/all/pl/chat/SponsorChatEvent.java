package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.classes.ChatType;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.system.LXConstants;
import cn.xgame.utils.Logs;
import cn.xgame.net.netty.Netty.RW;

/**
 * 发起聊天
 * @author deng		
 * @date 2015-7-25 上午10:04:22
 */
public class SponsorChatEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int axnId		= data.readInt();
		ChatType type 	= ChatType.fromNumber( axnId/LXConstants.CHAT_UID );
		String content 	= RW.readString(data);
		
		ErrorCode code 	= null;
		try {
			
			// 这里做一些限制的事情
			// TODO
			Logs.debug( player.getCtx(), "发起聊天" + axnId + ":" + content  );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( axnId );
			RW.writeString( buffer, content );
		}
		sendPackage( player.getCtx(), buffer );
		
		// 同步消息
		if( code == ErrorCode.SUCCEED ){
			ChatManager.o.synchronizeMsg( type, axnId, player, content );
		}
		
	}

}
