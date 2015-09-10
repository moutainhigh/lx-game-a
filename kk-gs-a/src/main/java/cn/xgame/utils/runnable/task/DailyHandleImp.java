package cn.xgame.utils.runnable.task;


import cn.xgame.a.player.PlayerManager;
import cn.xgame.utils.Logs;
import cn.xgame.utils.runnable.IThread;


/**
 * 每日处理 线程
 * @author Administrator
 *
 */
public class DailyHandleImp extends IThread{
	
	@Override
	public void run() {
		Logs.debug( "每日更新 --> 开始" );
		try {
			
			// 刷新每个玩家的 每日数据
			PlayerManager.o.allDailyHandle();
			
			
			
		} catch (Exception e) {
			Logs.error( "DailyHandleImp:" , e );
		}
		Logs.debug( "每日更新 --> 结束" );
	}

}
