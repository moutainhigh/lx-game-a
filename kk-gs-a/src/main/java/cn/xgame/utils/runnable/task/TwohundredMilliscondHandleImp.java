package cn.xgame.utils.runnable.task;

import cn.xgame.a.world.WorldManager;
import cn.xgame.utils.Logs;
import cn.xgame.utils.runnable.IThread;

/**
 * 200毫秒 执行一次
 * @author deng		
 * @date 2015-7-13 下午5:01:55
 */
public class TwohundredMilliscondHandleImp extends IThread{

	@Override
	public void run() {
		
		try {
			
			WorldManager.o.runDevelopment();
			
			
		} catch (Exception e) {
			Logs.error( "TwohundredMilliscondHandleImp:" , e );
		}
	}

}
