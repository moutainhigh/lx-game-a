package cn.xgame.utils.runnable;

import x.javaplus.util.Util.Time;
import cn.xgame.utils.runnable.task.DailyHandleImp;
import cn.xgame.utils.runnable.task.TwoMinuteHandleImp;



/**
 * 所有线程 管理 
 * @author DXF
 *
 */
public class ThreadManager {

	// 每日
	private static DailyHandleImp 		dailyHandleImp 		= null;
	
	// 每两分钟
	private static TwoMinuteHandleImp 	twoMinuteHandleImp	= null;
	
	
	/** 开启所有 线程 */
	public static void start(){
		
		// 没2分钟 刷新服务器人数 到登录服务器 2*60*1000
		twoMinuteHandleImp = new TwoMinuteHandleImp();
		twoMinuteHandleImp.start( 120000l );
		
		// 每日处理线程  24*60*60*1000
		dailyHandleImp = new DailyHandleImp();
		dailyHandleImp.start( Time.toWeehoursTime(), 86400000l );
		
		
	}
	
	/** 关闭所有 线程 */
	public static void stop(){
		
		twoMinuteHandleImp.stop();
		twoMinuteHandleImp = null;
		dailyHandleImp.stop();
		dailyHandleImp = null;
		
	}
	
}
