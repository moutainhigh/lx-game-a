package cn.xgame.a.world;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.entrepot.EntrepotPlanet;
import cn.xgame.a.world.planet.home.HomePlanet;

/**
 * 世界银河 星图  管理中心
 * @author deng		
 * @date 2015-6-18 下午4:14:21
 */
public class WorldManager {
	
	private WorldManager(){}
	public static WorldManager o = new WorldManager();
	
	// 所有 母星 列表
	private List<HomePlanet> homes = Lists.newArrayList();
	
	
	// 所有 中转星 列表
	private List<EntrepotPlanet> entrepots = Lists.newArrayList();
	
	/** 初始化 星图 */
	public void initialize(){
		
		HomePlanet home = new HomePlanet();
		homes.add( home );
		
		EntrepotPlanet entrepot = new EntrepotPlanet();
		entrepots.add( entrepot );
	}
	
	
	
	
	/**
	 * 分配母星
	 * @param player
	 * @param ip
	 */
	public void allotHomePlanet( Player player, String ip ){
		
		// 先根据ip获取对应母星
		HomePlanet home = getHomeInIP( ip );
		
		// 把玩家放入 母星
		home.add( player );
		
		// 初始化 领地
//		player.
	}
	
	// 根据ip获取 对应母星
	private HomePlanet getHomeInIP(String ip) {
		return homes.get(0);
	}
	


	
}
