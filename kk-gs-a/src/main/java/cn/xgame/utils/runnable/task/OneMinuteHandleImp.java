package cn.xgame.utils.runnable.task;

import x.javaplus.mysql.db.Dbcp;
import cn.xgame.utils.Logs;
import cn.xgame.utils.runnable.IThread;

/**
 * 一分钟执行一次
 * @author deng		
 * @date 2015-6-29 下午4:55:08
 */
public class OneMinuteHandleImp extends IThread {

	@Override
	public void run() {
		try {
			
			// 这里测试打印 数据库 使用情况
			Dbcp.print();
		
		
		} catch (Exception e) {
			Logs.error( "OneMinuteHandleImp:", e );
		}
	}

}
