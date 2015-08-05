package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.o.IAxnCrew;
import cn.xgame.a.chat.o.AxnInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

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
	 * @param to
	 * @param axnId
	 */
	public void run( Player to, int axnId ) {
		
		try {
			ByteBuf response = buildEmptyPackage( to.getCtx(), 1024 );
			
			response.writeInt( axnId );
			
			AxnInfo axn 			= ChatManager.o.getChatControl().getAXNInfo(axnId);
			List<IAxnCrew> crews 	= axn.getAxnCrews();
			byte size 				= 0;
			response.writeByte( size );
			for( IAxnCrew crew : crews ){
				if( crew.getUid().equals( to.getUID() ) )// 这里是自己就不显示
					continue;
				crew.buildTransformStream(response);
				++size;
			}
			
			response.setByte( 9, size );
			sendPackage( to.getCtx(), response );
			
		} catch (Exception e) {
			Logs.error( "Update_3010 ", e );
		}
	}

	/**
	 * 同步给其他玩家 提示有人加入频道了
	 * @param socket
	 * @param axnId
	 * @param to
	 */
	public void run( ChannelHandlerContext socket, int axnId, Player to ) {
		try {
			
			ByteBuf response = buildEmptyPackage( socket, 1024 );
			
			response.writeInt( axnId );
			response.writeByte( 1 );
			RW.writeString( response, to.getUID() );
			RW.writeString( response, to.getNickname() );
			response.writeInt( to.getHeadIco() );
			
			sendPackage( socket, response );
		
		} catch (Exception e) {
			Logs.error( "Update_3010 ", e );
		}
	}

}
