package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 修改群聊名字 通知
 * @author deng		
 * @date 2015-10-23 下午3:22:10
 */
public class Update_3012 extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	/**
	 * 通知
	 * @param axnId 频道ID
	 * @param newName 频道新名字
	 * @param nickname 修改人名字
	 * @param accept 接受者
	 */
	public void run(int axnId, String newName, String nickname, Player accept) {
	
		try {
			ByteBuf buffer = buildEmptyPackage( accept.getCtx(), 125 );
			buffer.writeInt( axnId );
			RW.writeString( buffer, newName );
			RW.writeString( buffer, nickname );
			sendPackage( accept.getCtx(), buffer );
		} catch (Exception e) {
			Logs.error( "Update_3012 ", e );
		}
	}

}
