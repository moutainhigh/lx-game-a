package cn.xgame.net.event.all.pl.task;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.fleet.o.FleetInfo;
import cn.xgame.a.player.task.TaskControl;
import cn.xgame.a.player.task.classes.ITask;
import cn.xgame.a.player.task.classes.TaskType;
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
		
		int snid 	= data.readInt();
		int npcid 	= data.readInt();
		int taskid	= data.readInt();
		
		ErrorCode code = null;
		try {
			TaskPo templet = CsvGen.getTaskPo(taskid);
			if( templet == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 判断 地点
			if( !isInSite( player, templet.needsite ) )
				throw new Exception( ErrorCode.CON_DISSATISFY.name() );
			// 判断 等级
			// TODO
			
			// 检测是否有这个任务 并且 在可接任务列表中删除掉
			TaskControl tasks = player.getTasks();
			if( !tasks.removeCanTask( snid, npcid, taskid ) )
				throw new Exception( ErrorCode.TASK_NOTEXIST.name() );
			
			// 创建一个任务
			TaskType type = TaskType.fromNumber( templet.type );
			ITask ret = type.create( templet );
			
			// 添加到已接任务列表
			tasks.addTask( ret );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( taskid );
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
