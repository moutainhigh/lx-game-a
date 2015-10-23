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
import cn.xgame.net.event.all.pl.update.Update_3013;
import cn.xgame.utils.Logs;

/**
 * 退出群聊频道
 * @author deng		
 * @date 2015-10-23 下午4:10:59
 */
public class ExitGroupAxnEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int axnId = data.readInt();
		
		ErrorCode code 	= null;
		AxnInfo axn		= null;
		try {
			
			axn = ChatManager.o.axns().getAXNInfo(axnId);
			if( axn == null )
				throw new Exception( ErrorCode.AXN_NOEXIST.name() );
			
			// 直接删除
			axn.removeCrew( player.getUID() );
			player.getChatAxns().removeAxn( ChatType.GROUP, axnId );
			
			Logs.debug( player.getCtx(), "退出 <" + axn.getName() + " 群聊>" );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 2 );
		buffer.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), buffer );
		
		// 这里同步给其他玩家
		if( code == ErrorCode.SUCCEED ){
			
			List<IAxnCrew> crews = axn.getAxnCrews();

			// 先检测是否只剩一个人了 如果只有一个人 那么就直接解散群聊
			if( crews.size() == 1 ){
				
				IAxnCrew crew = crews.get(0);
				Player accept = PlayerManager.o.getPlayerFmOnline( crew.getUid() );
				if( accept != null ){
					accept.getChatAxns().removeAxn( axn.getType(), axn.getAxnId() );
					((Update_3013)Events.UPDATE_3013.toInstance()).run( axnId, player.getNickname(), accept );
				}
				
				ChatManager.o.axns().removeAxn( axn.getAxnId() );
			}else{
				
				for( IAxnCrew crew : crews ){
					Player accept = PlayerManager.o.getPlayerFmOnline( crew.getUid() );
					if( accept == null ) 
						continue;
					((Update_3013)Events.UPDATE_3013.toInstance()).run( axnId, player.getNickname(), accept );
				}
			}
		}
	}

}
