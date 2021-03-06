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
 * 收集道具
 * @author deng		
 * @date 2015-10-25 上午2:05:41
 */
public class XiaohaoDaoju extends ICondition{
	
	// 需要消耗的道具 
	private List<IProp> props = Lists.newArrayList();
	
	public XiaohaoDaoju( ConType type, TaskcndPo templet ) {
		super(type,templet);
	}
	
	public boolean isComplete( Player player ){
		StarDepot depots = player.getDepots();
		for( IProp prop : props ){
			int count = 0;
			List<IProp> ls = depots.getPropsByNid(prop.getNid());
			for (IProp x : ls)
				count += x.getCount();
			if( count < prop.getCount() )
				return false;
		}
		return true;
	}
	
	@Override
	public void beginExecute(Player player) {
		String[] array = templet().needitem.split("\\|");
		for( String temp : array ){
			String str[] = temp.split(";");
			int id = Integer.parseInt( str[0] );
			int count = Integer.parseInt( str[1] );
			IProp prop = IProp.create(id, count);
			props.add(prop);
		}
	}
	
	@Override
	public void execute(Object[] objects) {
	}

	@Override
	public void endExecute(Player player) {
		// 删除收集的道具
		StarDepot depots = player.getDepots();
		for( IProp prop : props )
			depots.deductPropByNid(prop);
	}
}
