package cn.xgame.a.player.task.classes.condition;

import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.task.classes.ICondition;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.o.TaskcndPo;

/**
 * 到达地点 
 * @author deng		
 * @date 2015-10-25 上午1:55:25
 */
public class DaodaDidian extends ICondition{
	
	public DaodaDidian( ConType type, TaskcndPo templet ) {
		super(type,templet);
	}
	
	@Override
	public void beginExecute(Player player) {
		
	}
	
	@Override
	public void execute(Object[] objects) {
		int id = (Integer) objects[0];
		isComplete = templet().starid == id;
	}

	@Override
	public void endExecute(Player player) {
		
	}
	
}
