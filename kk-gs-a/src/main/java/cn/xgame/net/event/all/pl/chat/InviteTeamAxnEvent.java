package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;


import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.AxnControl;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.event.all.pl.update.Update_3020;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 邀请组队  <br>
 * 注：这里只是发送请求 并不会真真创建队伍
 * @author deng		
 * @date 2015-7-28 下午2:11:00
 */
public class InviteTeamAxnEvent extends IEvent {

	private final AxnControl chatControl = ChatManager.o.axns();
	
	@Override
	public void run( Player player, ByteBuf data ) throws IOException {
		
		byte fid 		= data.readByte();
		String ruid 	= RW.readString(data);
		
		ErrorCode code 	= null;
		try {
			// 获取舰队
			FleetInfo fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.isEmpty() || !fleet.isHover() )
				throw new Exception( ErrorCode.SHIP_NOTLEISURE.name() );
			
			// 这里证明已经有队伍了
			if( fleet.getAxnId() != -1 ){
				AxnInfo axn = chatControl.getAXNInfo( fleet.getAxnId() );
				// 判断舰船队伍是否已经满了
				if( axn.isMaxmember() )
					throw new Exception( ErrorCode.AXN_MAXMEMBER.name() );
				// 判断玩家是否已经在队伍列表中
				if( axn.isHave( ruid ) )
					throw new Exception( ErrorCode.AXN_HAVEIN.name() );
			}
			
			Player to = PlayerManager.o.getPlayerFmOnline(ruid);
			if( to == null )
				throw new Exception( ErrorCode.PLAYER_NOTEXIST.name() );
			
			// 然后给对方发送邀请组队信息
			((Update_3020)Events.UPDATE_3020.toInstance()).run( to, player, fid );
			
			Logs.debug( player.getCtx(), "邀请<" + to.getNickname() + ">组队" );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
	}

}
