package cn.xgame.a.player.task.classes.condition;



import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.task.classes.ICondition;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.o.TaskcndPo;

/**
 * 运输道具
 * @author deng		
 * @date 2015-12-1 下午5:23:29
 */
public class YunshuDaoju extends ICondition {
	
	// 道具UID列表
	private List<IProp> props = Lists.newArrayList();
	
	public YunshuDaoju(ConType type, TaskcndPo templet) {
		super(type, templet);
	}
	
	@Override
	public void beginExecute(Player player) {
		StarDepot depots = player.getDepots();
		if(templet().needitem.isEmpty())
			return;
		String[] array = templet().needitem.split("\\|");
		for( String temp : array ){
			String str[] = temp.split(";");
			int id = Integer.parseInt( str[0] );
			int count = Integer.parseInt( str[1] );
			IProp prop = IProp.create(id, count);
			props.add(prop);
			depots.appendProp(id, count);
		}
	}
	
	@Override
	public void execute(Object[] objects) {
		int id = (Integer) objects[0];
		isComplete = id == templet().starid;
	}

	@Override
	public void endExecute(Player player) {
		StarDepot depots = player.getDepots();
		for (IProp prop : props )
			depots.deductPropByNid(prop);
	}
}
