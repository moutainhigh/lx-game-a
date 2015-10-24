package cn.xgame.a.player.task.classes.condition;

import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.task.classes.ICondition;
import cn.xgame.config.o.TaskcndPo;

/**
 * 完成副本
 * @author deng		
 * @date 2015-10-25 上午2:04:14
 */
public class WanchengFuben extends ICondition{
	
	// 需要完成的副本ID
	private final int eid;
	
	// 完成次数
	private final int count;
	
	public WanchengFuben( ConType type, TaskcndPo templet ) {
		super(type,templet.id);
		String[] str = templet.value.split(";");
		eid 	= Integer.parseInt( str[0] );
		count 	= Integer.parseInt( str[1] );
	}

	@Override
	public boolean isComplete() {
		return count == getValue();
	}
	
	/**
	 * 执行 条件
	 * @param eid
	 */
	public void execute( int eid ){
		if( this.eid != eid || isComplete() )
			return ;
		addValue( 1 );
	}

}
