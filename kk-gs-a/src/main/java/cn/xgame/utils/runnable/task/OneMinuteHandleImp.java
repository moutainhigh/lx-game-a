package cn.xgame.utils.runnable.task;

import java.util.Collection;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
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
//			Dbcp.print();
			
			Collection<Player> values = PlayerManager.o.getOnlinePlayer().values();
			for( Player player : values ){
				player.getAccEctypes().run();
			}
		
		} catch (Exception e) {
			Logs.error( "OneMinuteHandleImp:", e );
		}
	}

}
