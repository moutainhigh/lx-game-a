package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.AxnControl;
import cn.xgame.a.chat.axn.classes.ChatType;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.event.all.pl.update.Update_3010;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 邀请加入群聊
 * @author deng		
 * @date 2015-7-25 上午10:42:07
 */
public class InviteGroupAxnEvent extends IEvent{

	private final AxnControl chatControl = ChatManager.o.getChatControl();
	
	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int axnId		= data.readInt();
		String ruid 	= RW.readString(data);
		
		ErrorCode code 	= null;
		AxnInfo axn		= null;
		Player to		= null;
		String axnName	= "";
		try {
			
			// 获取频道  判断各种条件
			axn = chatControl.getAXNInfo(axnId);
			if( axn == null && player.getChatAxns().axnIsMax( ChatType.GROUP ) )
				throw new Exception( ErrorCode.AXN_ISMAX.name() );
			
			// 看频道人数是不是已经满了
			if( axn != null && axn.isMaxmember() )
				throw new Exception( ErrorCode.AXN_MAXMEMBER.name() );
			
			// 看对方是不是已经在群聊里面了
			if( axn != null && axn.getAxnCrew( ruid ) != null )
				throw new Exception( ErrorCode.HAVEBEENIN_TEAM.name() );
			
			// 判断对方各种是否满
			to = PlayerManager.o.getPlayerFmOnline(ruid);
			if( to == null )
				throw new Exception( ErrorCode.PLAYER_NOTEXIST.name() );
			if( to.getChatAxns().axnIsMax( ChatType.GROUP ) )
				throw new Exception( ErrorCode.AXN_ISMAX.name() );
			
			// 设置名字
			axnName = axn == null ? player.getNickname() : axn.getName();
			
			Logs.debug( player.getCtx(), "邀请<" + to.getNickname() + ">加入 <"+axnName+" 群聊>" );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 125 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
		
		// 同步消息
		if( code == ErrorCode.SUCCEED ){
			((Update_3010)Events.UPDATE_3010.toInstance()).run( to, player, axnId, axnName );
		}
		
	}
	
}
