package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;
import cn.xgame.net.netty.Netty.RW;

/**
 * 同意和拒绝加入群聊 通知
 * @author deng		
 * @date 2015-10-23 下午2:58:56
 */
public class Update_3011 extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	/**
	 * 通知
	 * @param isAgree 是否同意
	 * @param accept 接受者
	 * @param player 被邀请者
	 * @param axnName 频道名字
	 * @param axnId 频道ID
	 */
	public void run( byte isAgree, Player accept, Player player, String axnName, int axnId ) {
		
		try {
			ByteBuf buffer = buildEmptyPackage( accept.getCtx(), 125 );
			buffer.writeByte( isAgree );
			RW.writeString( buffer, player.getNickname() );
			RW.writeString( buffer, axnName );
			if( isAgree == 1 ){
				buffer.writeInt( axnId );
			}
			sendPackage( accept.getCtx(), buffer );
		} catch (Exception e) {
			Logs.error( "Update_3011 ", e );
		}
	}

}
