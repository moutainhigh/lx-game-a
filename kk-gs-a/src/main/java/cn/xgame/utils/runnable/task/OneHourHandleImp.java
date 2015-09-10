package cn.xgame.utils.runnable.task;

import java.util.Collection;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.utils.Logs;
import cn.xgame.utils.runnable.IThread;

/**
 * 每小时
 * @author deng		
 * @date 2015-9-10 下午9:48:24
 */
public class OneHourHandleImp extends IThread{

	@Override
	public void run() {
		try {
			
			Collection<Player> values = PlayerManager.o.getOnlinePlayer().values();
			for( Player player : values ){
				player.getEctypes().initAllTimes();
			}
			
		} catch (Exception e) {
			Logs.error( "OneHourHandleImp:" , e );
		}
		
	}

}
