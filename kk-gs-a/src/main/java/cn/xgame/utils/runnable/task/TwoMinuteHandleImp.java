package cn.xgame.utils.runnable.task;

import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.ls.UpdatePeopleEvent;
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
			
			// 给登录服务器 更新 服务器人数
			((UpdatePeopleEvent)Events.UPDATA_PEOPLE.getEventInstance()).run();
			
			
			
			
		} catch (Exception e) {
			Logs.error( "TwoMinuteHandleImp:" , e );
		}
	}

}
