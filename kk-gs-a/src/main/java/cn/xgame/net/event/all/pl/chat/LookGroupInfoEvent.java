package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;

/**
 * 查看群聊成员
 * @author deng		
 * @date 2015-10-23 下午4:26:50
 */
public class LookGroupInfoEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int axnId = data.readInt();
		
		ErrorCode code 	= null;
		AxnInfo axn		= null;
		try {
			
			axn = ChatManager.o.getChatControl().getAXNInfo(axnId);
			if( axn == null )
				throw new Exception( ErrorCode.AXN_NOEXIST.name() );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			List<IAxnCrew> crews = axn.getAxnCrews();
			buffer.writeByte( crews.size() );
			for( IAxnCrew crew : crews ){
				RW.writeString( buffer, crew.getName() );
			}
		}
		sendPackage( player.getCtx(), buffer );
	}

}
