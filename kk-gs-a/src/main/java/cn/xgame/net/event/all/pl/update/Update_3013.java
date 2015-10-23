package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 有玩家退出群聊 通知
 * @author deng		
 * @date 2015-10-23 下午4:22:46
 */
public class Update_3013 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}
	
	/**
	 * 通知
	 * @param axnId 频道ID
	 * @param nickname 退出人名字
	 * @param accept 接受人
	 */
	public void run(int axnId, String nickname, Player accept) {
		
		try {
			ByteBuf buffer = buildEmptyPackage( accept.getCtx(), 50 );
			buffer.writeInt( axnId );
			RW.writeString( buffer, nickname );
			sendPackage( accept.getCtx(), buffer );
		} catch (Exception e) {
			Logs.error( "Update_3013 ", e );
		}
	}

}
