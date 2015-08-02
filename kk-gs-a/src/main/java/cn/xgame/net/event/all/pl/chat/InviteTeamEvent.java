package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.chat.AxnControl;
import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.o.AxnInfo;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.event.all.pl.update.Update_3020;
import cn.xgame.net.netty.Netty.RW;

/**
 * 邀请组队  <br>
 * 注：这里只是发送请求 并不会真真创建队伍
 * @author deng		
 * @date 2015-7-28 下午2:11:00
 */
public class InviteTeamEvent extends IEvent {

	private final AxnControl chatControl = ChatManager.o.getChatControl();
	
	@Override
	public void run( Player player, ByteBuf data ) throws IOException {
		
		int suid 		= data.readInt();
		String ruid 	= RW.readString(data);
		
		ErrorCode code 	= null;
		try {
			
			ShipInfo ship = player.getDocks().getShip(suid);
			if( ship == null )
				throw new Exception( ErrorCode.SHIP_NOTEXIST.name() );
			// 判断舰船是否可以出战
			if( !ship.isCanFighting() )
				throw new Exception( ErrorCode.SHIP_NOTFIGHTING.name() );
			if( ship.getTeamId() != 0 ){// 这里证明已经有队伍了
				AxnInfo axn = chatControl.getAXNInfo( ship.getTeamId() );
				// 判断舰船队伍是否已经满了
				if( axn.isMaxmember() )
					throw new Exception( ErrorCode.AXN_MAXMEMBER.name() );
				// 判断玩家是否已经在队伍列表中
				if( axn.getAxnCrew( ruid ) != null )
					throw new Exception( ErrorCode.HAVEBEENIN_TEAM.name() );
			}
			
			Player to = PlayerManager.o.getPlayerFmOnline(ruid);
			if( to == null )
				throw new Exception( ErrorCode.PLAYER_NOTEXIST.name() );
			// 判断对方是否还有船是空闲中
			if( !to.getDocks().isHaveLeisure() )
				throw new Exception( ErrorCode.SHIP_NOTLEISURE.name() );
			
			// 这里玩家记录一下 自己邀请过这个人
			ship.getTemprecord().recordInviteTeam( to );
			
			// 然后给对方发送邀请组队信息
			((Update_3020)Events.UPDATE_3020.getEventInstance()).run( to, player, suid );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
	}

}
