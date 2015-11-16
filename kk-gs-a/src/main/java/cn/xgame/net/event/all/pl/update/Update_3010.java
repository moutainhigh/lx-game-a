package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;
import cn.xgame.net.netty.Netty.RW;

/**
 * 邀请加入频道同步
 * @author deng		
 * @date 2015-8-2 下午3:15:16
 */
public class Update_3010 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	/**
	 * 同步给 被邀请者
	 * @param palyer
	 * @param axnId
	 */
	public void run( Player to, Player palyer, int axnId, String axnName ) {
		
		try {
			ByteBuf buffer = buildEmptyPackage( to.getCtx(), 50 );
			
			RW.writeString( buffer, palyer.getUID() );
			RW.writeString( buffer, palyer.getNickname() );
			buffer.writeInt( axnId );
			RW.writeString( buffer, axnName );
			
			sendPackage( to.getCtx(), buffer );
			
		} catch (Exception e) {
			Logs.error( "Update_3010 ", e );
		}
	}

}
