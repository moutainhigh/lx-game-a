package cn.xgame.net.event.all.pl.task;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.task.TaskControl;
import cn.xgame.a.player.task.classes.ICondition;
import cn.xgame.a.player.task.classes.ITask;
import cn.xgame.a.player.task.classes.TaskType;
import cn.xgame.a.player.task.info.CanTask;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.TaskPo;
import cn.xgame.net.event.IEvent;

/**
 * 接任务
 * @author deng		
 * @date 2015-10-25 上午12:32:26
 */
public class ReceiveTaskEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int taskid	= data.readInt();
		
		ErrorCode code = null;
		ITask ret = null;
		try {
			TaskPo templet = CsvGen.getTaskPo(taskid);
			if( templet == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 判断 地点
			if( !isInSite( player, templet.starid ) )
				throw new Exception( ErrorCode.CON_DISSATISFY.name() );
			
			TaskControl taskControl = player.getTasks();
			
			// 检测是否有这个任务 并且 在可接任务列表中删除掉
			CanTask ct = taskControl.getCanTask( taskid );
			if( ct == null )
				throw new Exception( ErrorCode.TASK_NOTEXIST.name() );
			
			// 先创建一个任务
			TaskType type = TaskType.fromNumber( templet.tasktype );
			ret = type.create( templet );
			
			if( ret.type() == TaskType.EVERYDAY ){// 每日任务 在可接任务中添加次数
				if( ret.templet().loopcount == ct.getLooptimes() )
					throw new Exception( ErrorCode.TIMES_LAZYWEIGHT.name() );
			}else{// 如果不是每日任务 那么就在可接任务列表删除掉
				taskControl.removeCanTask(taskid);
			}
			
			// 添加到已接任务列表
			taskControl.addTask( ret );
			
			// 这里测试 直接设置为可完成
			for( ICondition con : ret.getConditions() ){
				con.isComplete(true);
			}
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( taskid );
			buffer.writeInt( ret.getEndtime() );
		}
		sendPackage( player.getCtx(), buffer );
	}

	// 检测玩家是否在这个地点
	private boolean isInSite( Player player, int needsite ) {
		if( needsite == 0 )
			return true;
		List<FleetInfo> ls = player.getFleets().getFleetHavaShip();
		for( FleetInfo f : ls ){
			if( f.getBerthSnid() == needsite )
				return true;
		}
		return false;
	}

}
