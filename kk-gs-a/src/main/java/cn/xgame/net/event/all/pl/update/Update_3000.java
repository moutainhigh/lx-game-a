package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.chat.ChatType;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 聊天同步消息
 * @author deng		
 * @date 2015-7-25 上午10:30:52
 */
public class Update_3000 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	/**
	 * 同步聊天信息
	 * @param type 频道
	 * @param player 接收人
	 * @param sponsor 发起人
	 * @param content 内容
	 */
	public void run( ChatType type, Player player, Player sponsor, String content ) {
		
//		Axn : Byte( 1.世界 2.母星 3.组队 10.频道一 11.频道二 12.频道三 )
//		SprUid : String( 发起人唯一ID )
//		SprName : String( 发起人名字 )
//		Content : String( 内容 )
		try {
			ByteBuf response = buildEmptyPackage( player.getCtx(), 125 );
			
			response.writeByte( type.toNumber() );
			RW.writeString( response, sponsor.getUID() );
			RW.writeString( response, sponsor.getNickname() );
			RW.writeString( response, content );
			
			sendPackage( player.getCtx(), response );
		} catch (IOException e) {
			Logs.error( player, "Update_3000 " + e.getMessage() );
		}
	}

	
}
