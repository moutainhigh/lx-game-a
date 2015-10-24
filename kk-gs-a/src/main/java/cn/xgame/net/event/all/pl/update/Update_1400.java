package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;

/**
 * 任务完成通知
 * @author deng		
 * @date 2015-10-25 上午3:04:39
 */
public class Update_1400 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}
	
	/**
	 * 通知
	 * @param player 
	 * @param ret 道具更新个数
	 */
	public void run( Player player, List<IProp> ret ){
		try {
			ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
			buffer.writeInt( player.getCurrency() );
			buffer.writeByte( ret.size() );
			for( IProp prop : ret ){
				prop.putBaseBuffer(buffer);
				prop.buildTransformStream(buffer);
			}
			sendPackage( player.getCtx(), buffer );
		} catch (Exception e) {
			Logs.error( "Update_1400 ", e );
		}
	}
}
