package cn.xgame.utils.runnable;

import x.javaplus.util.Util.Time;
import cn.xgame.utils.runnable.task.DailyHandleImp;
import cn.xgame.utils.runnable.task.FiveMinuteHandleImp;
import cn.xgame.utils.runnable.task.OneMinuteHandleImp;
import cn.xgame.utils.runnable.task.OneSecondHandleImp;
import cn.xgame.utils.runnable.task.TwoMinuteHandleImp;
import cn.xgame.utils.runnable.task.TwohundredMilliscondHandleImp;



/**
 * 所有线程 管理 
 * @author DXF
 *
 */
public class ThreadManager {

	// 每日
	private static DailyHandleImp 		dailyHandleImp 		= null;
	
	// 每一分钟
	private static OneMinuteHandleImp 	oneMinuteHandleImp	= null;
	// 每两分钟
	private static TwoMinuteHandleImp 	twoMinuteHandleImp	= null;
	// 每五分钟
	private static FiveMinuteHandleImp  fiveMinuteHandleImp = null;
	// 每两百毫秒
	private static TwohundredMilliscondHandleImp twohundredMilliscondHandleImp = null;
	// 每一秒
	private static OneSecondHandleImp oneSecondHandleImp = null;
	
	/** 开启所有 线程 */
	public static void start(){
		
		// 每日处理线程  24*60*60*1000
		dailyHandleImp = new DailyHandleImp();
		dailyHandleImp.start( Time.toWeehoursTime(), 86400000l );

		// 每1分钟  1*60*1000
		oneMinuteHandleImp = new OneMinuteHandleImp();
		oneMinuteHandleImp.start( 60000l, 60000l );

		// 每2分钟  2*60*1000
		twoMinuteHandleImp = new TwoMinuteHandleImp();
		twoMinuteHandleImp.start( 120000l, 120000l );
		
		// 每5分钟 5*60*1000
		fiveMinuteHandleImp = new FiveMinuteHandleImp();
		fiveMinuteHandleImp.start( 300000l, 300000l );
		
		// 每200毫秒
		twohundredMilliscondHandleImp = new TwohundredMilliscondHandleImp();
		twohundredMilliscondHandleImp.start( 200l, 200l );
		
		// 每1秒
		oneSecondHandleImp = new OneSecondHandleImp();
		oneSecondHandleImp.start( 1000l, 1000l );
	}
	
	/** 关闭所有 线程 */
	public static void stop(){
		
		dailyHandleImp.stop();
		dailyHandleImp = null;
		oneMinuteHandleImp.stop();
		oneMinuteHandleImp = null;
		twoMinuteHandleImp.stop();
		twoMinuteHandleImp = null;
		fiveMinuteHandleImp.stop();
		fiveMinuteHandleImp = null;
		twohundredMilliscondHandleImp.stop();
		twohundredMilliscondHandleImp = null;
		oneSecondHandleImp.stop();
		oneSecondHandleImp = null;
		
	}
		
}
