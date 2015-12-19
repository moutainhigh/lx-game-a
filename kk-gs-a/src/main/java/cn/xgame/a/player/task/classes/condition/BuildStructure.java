package cn.xgame.a.player.task.classes.condition;

import java.util.List;

import cn.xgame.a.player.manor.info.IBuilding;
import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.task.classes.ICondition;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.o.TaskcndPo;

/**
 * 修建建筑
 * @author deng		
 * @date 2015-12-19 下午11:48:02
 */
public class BuildStructure extends ICondition {

	public BuildStructure(ConType type, TaskcndPo templet) {
		super(type, templet);
	}

	@Override
	public void beginExecute(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(Object[] objects) {
	}

	@Override
	public void endExecute(Player player) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 是否完成
	 */
	public boolean isComplete( Player player ){
		List<IBuilding> bs = player.getManors().getBuilds();
		for( IBuilding b : bs ){
			if( b.getNid() == templet().target )
				return true;
		}
		return false;
	}
	
}
