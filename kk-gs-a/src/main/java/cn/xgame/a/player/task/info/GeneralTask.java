package cn.xgame.a.player.task.info;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.task.classes.ITask;
import cn.xgame.a.player.task.classes.TaskType;
import cn.xgame.config.o.TaskPo;

/**
 * 常规任务
 * @author deng		
 * @date 2015-10-24 下午2:49:05
 */
public class GeneralTask extends ITask{
	
	public GeneralTask(TaskType type, TaskPo templet) {
		super(type,templet);
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		super.buildTransformStream(buffer);
	}
}
