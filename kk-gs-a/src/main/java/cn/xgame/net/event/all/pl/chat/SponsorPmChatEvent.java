package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;

/**
 * 发起私聊
 * @author deng		
 * @date 2015-7-25 上午10:42:07
 */
public class SponsorPmChatEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		String ruid = RW.readString(data);
		String content = RW.readString(data);
		
		ErrorCode code 	= null;
		Player to = null;
		try {
			to	 = PlayerManager.o.getPlayerFmOnline(ruid);
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 125 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
		
		// 同步消息
		if( code == ErrorCode.SUCCEED && to != null ){
			
			ChatManager.o.synPrivateMsg( player, to, content );
		}
		
	}
	
}
