package cn.xgame.a.world.planet.home;

import java.util.HashMap;
import java.util.Map;


import cn.xgame.a.player.u.Player;

/**
 * 一个母星
 * @author deng		
 * @date 2015-6-18 下午4:24:05
 */
public class HomePlanet {

	// 玩家列表
	private Map<String, Player> players = new HashMap<String, Player>();
	
	
	/**
	 * 添加一个 玩家
	 * @param player
	 */
	public void add( Player player ) {
		
		if( players.get(player.getUID()) == null )
			players.put( player.getUID(), player );
		
	}
	
	
	
	
}
