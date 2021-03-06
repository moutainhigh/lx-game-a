package cn.xgame.a.player.task.classes.condition;

import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.task.classes.ICondition;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.o.TaskcndPo;

/**
 * 完成副本
 * @author deng		
 * @date 2015-10-25 上午2:04:14
 */
public class WanchengFuben extends ICondition{
	
	public WanchengFuben( ConType type, TaskcndPo templet ) {
		super(type,templet);
	}

	@Override
	public void beginExecute(Player player) {
		
	}
	
	@Override
	public void execute(Object[] objects) {
		int id = (Integer) objects[0];
		isComplete = id == templet().target;
	}

	@Override
	public void endExecute(Player player) {
		
	}

}
