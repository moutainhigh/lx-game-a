package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.a.world.planet.home.o.OustChild;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 2252 驱逐元老列表更新包
 * @author deng		
 * @date 2015-7-11 下午1:16:18
 */
public class Update_2252 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}
	public void run( Player player, int isAdd, OustChild oust, Child x ) {
		try {
			ByteBuf response = buildEmptyPackage( player.getCtx(), 50 );
			response.writeByte( isAdd );
			RW.writeString( response, x.getUID() );
			if( isAdd == 1 ){
				RW.writeString( response, x.getName() );
				response.writeInt( x.getContribution() );
				response.writeShort( x.getPrivilege() );
				response.writeInt( x.getSponsors() );
				response.writeInt( x.getPasss() );
				RW.writeString( response, oust.getExplain() );
				RW.writeString( response, oust.getVote().getSponsorName() );
			}
			sendPackage( player.getCtx(), response );
		} catch (IOException e) {
			Logs.error( player, "Update_2242 " + e.getMessage() );
		}
	}

}
