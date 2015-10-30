package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 有人同意和拒绝出击
 * @author deng		
 * @date 2015-10-30 下午5:42:12
 */
public class Update_1221 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}
	
	/**
	 * 通知
	 * @param ctx
	 * @param fid
	 * @param uid
	 * @param isAgree
	 */
	public void run(ChannelHandlerContext ctx, byte fid, String uid, byte isAgree) {
		
		try {
			ByteBuf buffer = buildEmptyPackage( ctx, 50 );
			buffer.writeByte(fid);
			RW.writeString(buffer, uid);
			buffer.writeByte(isAgree);
			sendPackage( ctx, buffer );
		} catch (Exception e) {
			Logs.error( "Update_1221 ", e );
		}
	}

}
