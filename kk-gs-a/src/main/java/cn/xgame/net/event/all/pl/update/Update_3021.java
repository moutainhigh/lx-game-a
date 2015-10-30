package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 同意和拒绝组队 通知
 * @author deng		
 * @date 2015-8-2 下午11:15:07
 */
public class Update_3021 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}
	
	/**
	 * 通知
	 * @param isAgree 是否同意
	 * @param player 被邀请者
	 * @param fid 邀请者舰队ID
	 * @param sponsor 邀请者
	 * @param axnId 频道ID
	 */
	public void run( int isAgree, Player player, byte fid, Player sponsor, int axnId ) {
		
		try {
			ByteBuf buffer = buildEmptyPackage( sponsor.getCtx(), 125 );
			buffer.writeByte( isAgree );
			RW.writeString( buffer, player.getNickname() );
			buffer.writeByte( fid );
			if( isAgree == 1 ){
				buffer.writeInt( axnId );
				RW.writeString( buffer, player.getUID() );
				buffer.writeInt( player.getHeadIco() );
			}
			sendPackage( sponsor.getCtx(), buffer );
		} catch (Exception e) {
			Logs.error( "Update_3021 ", e );
		}
	}

}
