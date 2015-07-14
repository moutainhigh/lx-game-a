package cn.xgame.utils.runnable.task;

import cn.xgame.a.world.WorldManager;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.ls.UpdatePeopleEvent;
import cn.xgame.utils.Logs;
import cn.xgame.utils.runnable.IThread;

/**
 * 每五分钟执行一次
 * @author deng		
 * @date 2015-6-29 上午10:29:50
 */
public class FiveMinuteHandleImp extends IThread{

	@Override
	public void run() {
		try {
			
			// 给登录服务器 更新 服务器人数
			((UpdatePeopleEvent)Events.UPDATA_PEOPLE.getEventInstance()).run();
			
			// 
			WorldManager.o.runUpdateInstitution();
			
		} catch (Exception e) {
			Logs.error( "FiveMinuteHandleImp:" , e );
		}
	}

}
