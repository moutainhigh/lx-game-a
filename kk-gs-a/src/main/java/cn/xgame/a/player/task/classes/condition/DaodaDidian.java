package cn.xgame.a.player.task.classes.condition;

import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.task.classes.ICondition;
import cn.xgame.config.o.TaskcndPo;

/**
 * 到达地点 
 * @author deng		
 * @date 2015-10-25 上午1:55:25
 */
public class DaodaDidian extends ICondition{
	
	// 需要到达的星球ID
	private final int sid;
	
	public DaodaDidian( ConType type, TaskcndPo templet ) {
		super(type,templet.id);
		sid = Integer.parseInt( templet.value );
	}

	@Override
	public boolean isComplete() {
		return sid == getValue();
	}
	
	/**
	 * 执行 条件
	 * @param eid
	 */
	public void execute( int sid ){
		if( this.sid != sid || isComplete() )
			return ;
		setValue(sid);
	}
	
}
