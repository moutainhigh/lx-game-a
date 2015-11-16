package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.classes.ChatType;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.event.all.pl.update.Update_3011;
import cn.xgame.utils.Logs;
import cn.xgame.net.netty.Netty.RW;

/**
 * 是否同意加入群聊
 * @author deng
 * @date 2015-8-2 下午4:47:11
 */
public class AnswerGroupAxnEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte isAgree	= data.readByte(); 
		int axnId 		= data.readInt();
		String ruid		= RW.readString(data);
		
		ErrorCode code 	= null;
		Player sponsor	= null;
		AxnInfo axn		= null;
		try {
			sponsor = PlayerManager.o.getPlayerFmOnline(ruid);
			if( sponsor == null )
				throw new Exception( ErrorCode.PLAYER_NOTEXIST.name() );
			axn = ChatManager.o.axns().getAXNInfo(axnId);

			if( isAgree == 0 )
				throw new Exception( ErrorCode.REJECTPARTY.name() );
			
			// 判断 频道是否已经满了
			if( player.getChatAxns().axnIsMax( ChatType.GROUP ) )
				throw new Exception( ErrorCode.AXN_ISMAX.name() );
			
			// 获取频道
			if( axn != null ){
				
				// 判断频道人数是不是已经满了
				if( axn.isMaxmember() )
					throw new Exception( ErrorCode.AXN_MAXMEMBER.name() );
				
				// 判断是否已经在该频道了
				if( axn.isHave( player ) )
					throw new Exception( ErrorCode.AXN_HAVEIN.name() );
			} else {
				
				// 如果没有频道就创建一个
				axn = ChatManager.o.axns().createAxn( ChatType.GROUP );
				axn.setName( sponsor.getNickname() );
				axn.appendGroupCrew( sponsor );
				sponsor.getChatAxns().appendAxn( ChatType.GROUP, axnId );
			}
			
			// 加入频道
			axn.appendGroupCrew( player );
			player.getChatAxns().appendAxn( ChatType.GROUP, axnId );
			
			Logs.debug( player.getCtx(), (isAgree == 1 ? "同意" : "拒绝") + " <" + sponsor.getNickname() + "> 的组队邀请" );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( axn.getAxnId() );
			RW.writeString( buffer, axn.getName() );
		}
		sendPackage( player.getCtx(), buffer );
		
		// 只有同意才同步给所有玩家
		if( code == ErrorCode.SUCCEED){
			
			List<IAxnCrew> crews = axn.getAxnCrews();
			for( IAxnCrew crew : crews ){
				if( crew.getUid().equals( player.getUID() ) )
					continue;
				Player accept = PlayerManager.o.getPlayerFmOnline( crew.getUid() );
				if( accept == null ) 
					continue;
				((Update_3011)Events.UPDATE_3011.toInstance()).run( isAgree, accept, player, axn.getName(), axn.getAxnId() );
			}
			
			ChatManager.o.axns().update(axn);
		}
		// 如果是拒绝那么直给邀请者回复
		if( code == ErrorCode.REJECTPARTY ){
			String axnName = axn == null ? sponsor.getNickname() : axn.getName();
			((Update_3011)Events.UPDATE_3011.toInstance()).run( isAgree, sponsor, player, axnName, 0 );
		}
	}

}
