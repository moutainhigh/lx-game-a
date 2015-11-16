package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.data.building.UnBuildings;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;
import cn.xgame.net.netty.Netty.RW;

/**
 * 2242 星球建筑更新包
 * @author deng		
 * @date 2015-7-10 下午4:47:13
 */
public class Update_2241 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	public void run(Player player, int status, UnBuildings build ) {
		
		try {
			ByteBuf response = buildEmptyPackage( player.getCtx(), 15 );
			response.writeByte( status );
			response.writeInt( build.templet().id );
			response.writeByte( build.getIndex() );
			if( status == 1 )
				RW.writeString( response, build.getVote().getSponsorName() );
			if( status == 2 ){
				response.writeByte( build.getVote().getAgreePrivileges()/100 );
				response.writeByte( build.getVote().getDisagreePrivileges()/100 );
			}
			if( status == 3 )
				response.writeInt( build.getEndtime() );
			sendPackage( player.getCtx(), response );
		} catch (IOException e) {
			Logs.error( player, "Update_2241 " + e.getMessage() );
		}
	}

	
	
	
}
