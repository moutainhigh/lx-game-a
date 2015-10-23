package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.AxnControl;
import cn.xgame.a.chat.axn.classes.ChatType;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.event.all.pl.update.Update_3012;
import cn.xgame.net.netty.Netty.RW;

/**
 * 修改群聊名字
 * @author deng		
 * @date 2015-10-23 下午2:19:42
 */
public class AlterGroupNameEvent extends IEvent{

	private final AxnControl chatControl = ChatManager.o.getChatControl();
	
	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int axnId = data.readInt();
		String newName = RW.readString(data);
		
		ErrorCode code 	= null;
		AxnInfo axn 	= null;
		try {
			axn = chatControl.getAXNInfo(axnId);
			if( axn == null )
				throw new Exception( ErrorCode.AXN_NOEXIST.name() );
			
			// 判断是否群聊频道
			if( axn.getType() != ChatType.GROUP )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 直接设置新的名字 
			axn.setName( newName );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( axnId );
			RW.writeString( buffer, newName );
		}
		sendPackage( player.getCtx(), buffer );
		
		// 这里同步给其他玩家
		if( code == ErrorCode.SUCCEED ){
			List<IAxnCrew> crews = axn.getAxnCrews();
			for( IAxnCrew crew : crews ){
				if( crew.getUid().equals( player.getUID() ) )
					continue;
				Player accept = PlayerManager.o.getPlayerFmOnline( crew.getUid() );
				if( accept == null ) 
					continue;
				((Update_3012)Events.UPDATE_3012.toInstance()).run( axnId, newName, player.getNickname(), accept );
			}
		}
	}

}
