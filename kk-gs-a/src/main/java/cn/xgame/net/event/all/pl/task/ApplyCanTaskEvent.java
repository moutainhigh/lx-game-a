package cn.xgame.net.event.all.pl.task;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.player.task.star.CanTask;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请可接任务列表
 * @author deng		
 * @date 2015-10-25 上午12:00:31
 */
public class ApplyCanTaskEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int snid 	= data.readInt();
		int npcid 	= data.readInt();
		
		// 获取列表
		List<CanTask> ret = player.getTasks().getCanTask( snid, npcid );
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeByte( ret == null ? 0 : ret.size() );
		if( ret != null ){
			for( CanTask task : ret ){
				buffer.writeInt( task.getId() );
				buffer.writeInt( task.getEndtime() );
			}
		}
		sendPackage( player.getCtx(), buffer );
	}

}
