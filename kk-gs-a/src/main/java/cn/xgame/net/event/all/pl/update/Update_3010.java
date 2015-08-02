package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.o.AxnCrew;
import cn.xgame.a.chat.o.AxnInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 创建频道同步
 * @author deng		
 * @date 2015-8-2 下午3:15:16
 */
public class Update_3010 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	/**
	 * 同步创建频道
	 * @param to
	 * @param axnId
	 */
	public void run( Player to, int axnId ) {
		
		try {
			ByteBuf response = buildEmptyPackage( to.getCtx(), 1024 );
			
			response.writeInt( axnId );
			
			AxnInfo axn 		= ChatManager.o.getChatControl().getAXNInfo(axnId);
			List<AxnCrew> crews = axn.getAxnCrews();
			response.writeByte( crews.size() );
			for( AxnCrew crew : crews ){
				RW.writeString( response, crew.getUid() );
				RW.writeString( response, crew.getName() );
				response.writeInt( crew.getHeadIco() );
			}
			
			sendPackage( to.getCtx(), response );
			
		} catch (Exception e) {
			Logs.error( "Update_3010 ", e );
		}
	}

}
