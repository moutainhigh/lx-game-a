package cn.xgame.utils.runnable.task;

import cn.xgame.a.world.WorldManager;
import cn.xgame.utils.Logs;
import cn.xgame.utils.runnable.IThread;

/**
 * 每两分钟 执行 一次
 * @author deng		
 * @date 2015-6-16 上午10:50:38
 */
public class TwoMinuteHandleImp extends IThread{

	@Override
	public void run() {
		try {
			
			// 每个母星的 特产 
			WorldManager.o.run();
			
		} catch (Exception e) {
			Logs.error( "TwoMinuteHandleImp:" , e );
		}
	}

}
