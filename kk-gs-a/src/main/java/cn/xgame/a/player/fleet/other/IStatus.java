package cn.xgame.a.player.fleet.other;

import cn.xgame.a.ITransformStream;

/**
 * 舰队状态基类
 * @author deng		
 * @date 2015-9-11 上午12:35:31
 */
public abstract class IStatus implements ITransformStream{

	
	/**
	 * 状态
	 * @return
	 */
	public abstract StatusType type();
	
	
	
}
