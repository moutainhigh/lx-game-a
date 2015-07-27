package cn.xgame.utils.runnable.task;

import x.javaplus.util.Util.Sleep;
import cn.xgame.a.world.WorldManager;
import cn.xgame.utils.Logs;
import cn.xgame.utils.runnable.IThread;

/**
 * 每一秒
 * @author deng		
 * @date 2015-7-27 上午11:31:44
 */
public class OneSecondHandleImp extends IThread {

	@Override
	public void run() {
		try {
			
			WorldManager.o.runDevelopment();
			
			
		} catch (Exception e) {
			Logs.error( "OneSecondHandleImp:" , e );
			Sleep.sleep( 3000 );
		}
	}

}
