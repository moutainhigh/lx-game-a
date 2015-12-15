package cn.xgame.net.event.all.pl.task;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.task.TaskControl;
import cn.xgame.a.player.task.classes.ITask;
import cn.xgame.a.player.task.classes.TaskType;
import cn.xgame.a.player.task.info.CanTask;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 完成任务
 * @author deng		
 * @date 2015-12-1 下午4:35:06
 */
public class CompleteTaskEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		int taskid = data.readInt();
		
		ErrorCode code = null;
		try {
			
			TaskControl taskControl = player.getTasks();
			// 获取任务
			ITask task = taskControl.getYetInTask(taskid);
			if( task == null )
				throw new Exception( ErrorCode.TASK_NOTEXIST.name() );
			// 判断是否限时任务
			if( task.getEndtime() != 0 ){
				int t = (int) (System.currentTimeMillis()/1000);
				if( t >= task.getEndtime() )
					throw new Exception( ErrorCode.OVERTIME.name() );
			}
			// 判断是否完成
			if( !task.isComplete(player) )
				throw new Exception( ErrorCode.NOT_COMPLETE.name() );
			
			// 执行奖励
			task.executeAward(player);
			// 执行结束
			task.executeEnd(player);
			
			// 如果不是日常任务 那么就再次放入到可接任务列表
			if( task.type() == TaskType.EVERYDAY ){
				CanTask ct = taskControl.getCanTask(taskid);
				if( ct != null ) ct.addLooptimes(1);
			}else{
				// 添加后续任务
				taskControl.addCanTask( task.templet().lasttask );
				// 添加到完成列表中
				taskControl.addCompletTask( taskid );
			}
			
			// 最后删除这个任务
			taskControl.removeYetTask(taskid);
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage(player.getCtx(), 16);
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( taskid );
			buffer.writeInt( player.getCurrency() );
			buffer.writeShort( player.getLevel() );
			buffer.writeInt( player.getExp() );
		}
		sendPackage(player.getCtx(), buffer);
	}

}
