package cn.xgame.a.player.task.classes;

import java.util.HashMap;
import java.util.Map;

import cn.xgame.a.player.task.classes.condition.DaodaDidian;
import cn.xgame.a.player.task.classes.condition.WanchengFuben;
import cn.xgame.a.player.task.classes.condition.XiaohaoDaoju;
import cn.xgame.config.o.TaskcndPo;

/**
 * 条件类型
 * @author deng		
 * @date 2015-10-25 上午1:58:10
 */
public enum ConType {
	
	/**
	 * 到达地点
	 */
	DAODADIDIAN(1) {
		@Override
		public ICondition create(TaskcndPo templet) {
			return new DaodaDidian( DAODADIDIAN, templet );
		}
	},
	
	/**
	 * 完成副本
	 */
	WANCHENGFUBEN(2) {
		@Override
		public ICondition create(TaskcndPo templet) {
			return new WanchengFuben( WANCHENGFUBEN, templet );
		}
	},
	
	/**
	 * 消耗道具
	 */
	XIAOHAODAOJU(3) {
		@Override
		public ICondition create(TaskcndPo templet) {
			return new XiaohaoDaoju(XIAOHAODAOJU,templet);
		}
	};

	
	private final byte	number;
	private static final Map<Byte, ConType> numToEnum = new HashMap<Byte, ConType>();
	ConType( int n ){
		number = (byte) n;
	}
	static{
		for( ConType a : values() ){
			ConType p = numToEnum.put( a.number, a );
			if( p != null ){
				throw new RuntimeException( a.number + "重复了" );
			}
		}
	}
	public byte toNumber(){ return number; }
	public static ConType fromNumber( int n ){
		return numToEnum.get( (byte)n );
	}
	
	/**
	 * 创建一个条件
	 * @param templet
	 * @return
	 */
	public abstract ICondition create( TaskcndPo templet );
	
}
