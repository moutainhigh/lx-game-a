package cn.xgame.net.event.all.pl.mail;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.player.mail.classes.IMail;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请邮件列表
 * @author deng		
 * @date 2015-10-14 下午5:43:13
 */
public class ApplyMailEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte page = data.readByte();
		
		List<IMail> list = player.getMails().getMailsByPage( page ); 
		
		ByteBuf respon = buildEmptyPackage( player.getCtx(), 256 );
		respon.writeByte( list.size() );
		for( IMail mail : list ){
			mail.putBuffer(respon);
		}
		sendPackage( player.getCtx(), respon );
		
	}

}
