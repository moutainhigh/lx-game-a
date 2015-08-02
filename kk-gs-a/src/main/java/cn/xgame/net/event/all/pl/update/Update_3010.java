package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.o.IAxnCrew;
import cn.xgame.a.chat.o.AxnInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
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
			List<IAxnCrew> crews = axn.getAxnCrews();
			response.writeByte( crews.size()-1 );// 这里要减去自己
			for( IAxnCrew crew : crews ){
				if( crew.getUid().equals( to.getUID() ) )
					continue;
				crew.buildTransformStream(response);
			}
			
			sendPackage( to.getCtx(), response );
			
		} catch (Exception e) {
			Logs.error( "Update_3010 ", e );
		}
	}

}
