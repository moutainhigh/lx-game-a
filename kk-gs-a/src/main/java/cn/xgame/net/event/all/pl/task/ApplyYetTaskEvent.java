package cn.xgame.net.event.all.pl.task;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.player.task.TaskControl;
import cn.xgame.a.player.task.classes.ITask;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请已接任务列表
 * @author deng		
 * @date 2015-10-25 上午12:29:09
 */
public class ApplyYetTaskEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		// 先刷新一下
		TaskControl control = player.getTasks();
		control.update();
		
		// 获取列表
		List<ITask> tasks = control.getYetInTask();
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 256 );
		buffer.writeByte( tasks.size() );
		for( ITask task : tasks ){
			task.buildTransformStream(buffer);
		}
		sendPackage( player.getCtx(), buffer );
	}
	
}
