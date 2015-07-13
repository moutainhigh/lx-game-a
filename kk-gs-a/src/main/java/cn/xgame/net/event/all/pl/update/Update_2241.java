package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.data.building.UnBuildings;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 2242 星球建筑投票中列表更新包
 * @author deng		
 * @date 2015-7-10 下午4:47:13
 */
public class Update_2241 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	public void run(Player player, int status, UnBuildings voteBuild) {
		
		try {
			ByteBuf response = buildEmptyPackage( player.getCtx(), 15 );
			response.writeByte( status );
			response.writeInt( voteBuild.templet().id );
			response.writeByte( voteBuild.getIndex() );
			if( status == 1 )
				RW.writeString( response, voteBuild.getVote().getSponsorName() );
			if( status == 3 )
				response.writeInt( voteBuild.getPastTime() );
			sendPackage( player.getCtx(), response );
		} catch (IOException e) {
			Logs.error( player, "Update_2242 " + e.getMessage() );
		}
	}

	
	
	
}
