package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.chat.AxnControl;
import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.o.AxnInfo;
import cn.xgame.a.chat.o.ChatType;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.event.all.pl.update.Update_3010;
import cn.xgame.net.netty.Netty.RW;

/**
 * 邀请玩家加入临时频道
 * @author deng
 * @date 2015-8-2 下午4:47:11
 */
public class InviteTempAxnEvent extends IEvent {

	private final AxnControl chatControl = ChatManager.o.getChatControl();

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int axnId 	= data.readInt();
		String ruid	= RW.readString(data);
		
		ErrorCode code 	= null;
		Player to 		= null;
		try {
			
			// 判断该频道是否人数上限了
			AxnInfo axn = chatControl.getAXNInfo(axnId);
			if( axn == null )
				throw new Exception( ErrorCode.AXN_NOEXIST.name() );
			if( axn.isMaxmember() )
				throw new Exception( ErrorCode.AXN_MAXMEMBER.name() );
			
			// 判断邀请人是否有权限
			if( !axn.isHavePrivilege( player ) )
				throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );
			
			to	 = PlayerManager.o.getPlayerFmOnline(ruid);
			if( to == null )
				throw new Exception( ErrorCode.PLAYER_NOTEXIST.name() );
			// 判断被邀请者频道是否已经满了
			if( to.getChatAxns().axnIsMax( ChatType.TEMPAXN ) )
				throw new Exception( ErrorCode.AXN_ISMAX.name() );
			
			// 加入频道
			axn.appendTempCrew( to );
			
			// 记录到玩家身上
			to.getChatAxns().appendAxn( ChatType.TEMPAXN, axnId );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 125 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
		
		// 同步消息
		if( code == ErrorCode.SUCCEED && to != null ){
			((Update_3010)Events.UPDATE_3010.getEventInstance()).run( to, axnId );
		}
	}

}
