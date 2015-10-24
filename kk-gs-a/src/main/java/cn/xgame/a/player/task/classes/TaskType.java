package cn.xgame.a.player.task.classes;

import java.util.HashMap;
import java.util.Map;

import cn.xgame.a.player.task.info.GeneralTask;
import cn.xgame.config.o.TaskPo;


/**
 * 任务类型
 * @author deng		
 * @date 2015-10-24 下午3:02:41
 */
public enum TaskType {
	
	/** 常规任务 */
	GENERAL(1) {
		@Override
		public ITask create(TaskPo templet) {
			return new GeneralTask( GENERAL, templet );
		}
	};
	
	private final byte	number;
	private static final Map<Byte, TaskType> numToEnum = new HashMap<Byte, TaskType>();
	TaskType( int n ){
		number = (byte) n;
	}
	static{
		for( TaskType a : values() ){
			TaskType p = numToEnum.put( a.number, a );
			if( p != null ){
				throw new RuntimeException( a.number + "重复了" );
			}
		}
	}
	public byte toNumber(){ return number; }
	public static TaskType fromNumber( int n ){
		return numToEnum.get( (byte)n );
	}
	
	/**
	 * 创建一个任务 
	 * @param templet
	 * @return
	 */
	public abstract ITask create( TaskPo templet ) ;
	
}
