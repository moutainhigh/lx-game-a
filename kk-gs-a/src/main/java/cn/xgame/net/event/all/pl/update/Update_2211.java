package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.data.specialty.Specialty;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;

/**
 * 更新包 星球特产
 * @author deng		
 * @date 2015-6-29 下午5:46:08
 */
public class Update_2211 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	public void run( Player player, Specialty spe ) {
		try {
			
			ByteBuf response = buildEmptyPackage( player.getCtx(), 8 );
			
			response.writeInt( spe.toProp().getNid() );
			response.writeInt( spe.toProp().getCount() );
		
			sendPackage( player.getCtx(), response );
			
		} catch (IOException e) {
			Logs.error( "Update_2211:", e );
		}
	}

}
