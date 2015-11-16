package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.data.tech.UnTechs;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;
import cn.xgame.net.netty.Netty.RW;

/**
 * 2231 星球科技更新包
 * @author deng		
 * @date 2015-7-13 下午2:14:19
 */
public class Update_2231 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}
	public void run(Player player, int status, UnTechs tech ) {
		
		try {
			ByteBuf response = buildEmptyPackage( player.getCtx(), 15 );
			response.writeByte( status );
			response.writeInt( tech.templet().id );
			if( status == 1 )
				RW.writeString( response, tech.getVote().getSponsorName() );
			if( status == 2 ){
				response.writeByte( tech.getVote().getAgreePrivileges()/100 );
				response.writeByte( tech.getVote().getDisagreePrivileges()/100 );
			}
			if( status == 3 )
				response.writeInt( tech.getEndtime() );
			sendPackage( player.getCtx(), response );
		} catch (IOException e) {
			Logs.error( player, "Update_2231.run at " + e.getMessage() );
		}
		
	}

}
