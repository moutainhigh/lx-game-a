package cn.xgame.a.player.task.classes;

import java.util.HashMap;
import java.util.Map;

import cn.xgame.a.player.task.classes.condition.DaodaDidian;
import cn.xgame.a.player.task.classes.condition.DuiHua;
import cn.xgame.a.player.task.classes.condition.WanchengFuben;
import cn.xgame.a.player.task.classes.condition.XiaohaoDaoju;
import cn.xgame.a.player.task.classes.condition.YunshuDaoju;
import cn.xgame.config.o.TaskcndPo;

/**
 * 条件类型
 * @author deng		
 * @date 2015-10-25 上午1:58:10
 */
public enum ConType {
	
	/**
	 * 对话
	 */
	DUIHUA(1) {
		@Override
		public ICondition create(TaskcndPo templet) {
			return new DuiHua( DUIHUA, templet );
		}
	},
	
	/**
	 * 到达地点
	 */
	DAODADIDIAN(2) {
		@Override
		public ICondition create(TaskcndPo templet) {
			return new DaodaDidian( DAODADIDIAN, templet );
		}
	},
	
	/**
	 * 完成副本
	 */
	WANCHENGFUBEN(3) {
		@Override
		public ICondition create(TaskcndPo templet) {
			return new WanchengFuben( WANCHENGFUBEN, templet );
		}
	},
	
	/**
	 * 运输道具
	 */
	YUNSHUDAOJU(4) {
		@Override
		public ICondition create(TaskcndPo templet) {
			return new YunshuDaoju( YUNSHUDAOJU, templet );
		}
	},
	
	/**
	 * 收集道具
	 */
	SHOUJIDAOJU(5) {
		@Override
		public ICondition create(TaskcndPo templet) {
			return new XiaohaoDaoju( SHOUJIDAOJU, templet );
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
