package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;

/**
 * 星球资源更新包
 * @author deng		
 * @date 2015-6-30 上午10:08:09
 */
public class Update_2221 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	public void run( Player player, List<IProp> props ) {
		try {
			ByteBuf response = buildEmptyPackage( player.getCtx(), 15 );
			response.writeByte( props.size() );
			for( IProp prop : props )
				prop.putBaseBuffer(response);
			sendPackage( player.getCtx(), response );
		} catch (IOException e) {
			Logs.error( player, "Update_2221.run at " + e.getMessage() );
		}
	}

}
