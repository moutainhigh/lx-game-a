package cn.xgame.a.player.task.classes.condition;

import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.task.classes.ICondition;
import cn.xgame.config.o.TaskcndPo;

/**
 * 消耗道具
 * @author deng		
 * @date 2015-10-25 上午2:05:41
 */
public class XiaohaoDaoju extends ICondition{
	
	// 需要消耗的道具ID 
	private final int pid;
	
	// 消耗个数
	private final int count;
	
	// 完成后是否删除
	private final boolean isRemove;
	
	
	public XiaohaoDaoju( ConType type, TaskcndPo templet ) {
		super(type,templet.id);
		String[] str = templet.value.split(";");
		pid 		= Integer.parseInt( str[0] );
		count 		= Integer.parseInt( str[1] );
		isRemove 	= Integer.parseInt( str[2] ) == 1;
	}

	@Override
	public boolean isComplete() {
		return count == getValue();
	}

	public int getPid() {
		return pid;
	}
	public int getCount() {
		return count;
	}
	public boolean isRemove() {
		return isRemove;
	}
}
