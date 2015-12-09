package cn.xgame.a.player.task.classes.condition;

import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.task.classes.ICondition;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.o.TaskcndPo;

/**
 * 对话
 * @author deng		
 * @date 2015-12-1 下午4:55:06
 */
public class DuiHua extends ICondition {
	
	public DuiHua( ConType type, TaskcndPo templet ) {
		super(type,templet);
	}
	
	@Override
	public void beginExecute(Player player) {
		
	}
	
	@Override
	public void execute(Object[] objects) {
		int sid = (Integer) objects[0];//星球ID
		int npcid = (Integer) objects[1];//NPCid
		isComplete = ( (templet().starid == 0 || templet().starid == sid) && templet().target == npcid );
	}

	@Override
	public void endExecute(Player player) {
		
	}


	
}
