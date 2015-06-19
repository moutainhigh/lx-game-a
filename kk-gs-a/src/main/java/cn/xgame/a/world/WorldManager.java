package cn.xgame.a.world;

import java.util.HashMap;
import java.util.Map;

import cn.xgame.a.world.planet.entrepot.EntrepotPlanet;
import cn.xgame.a.world.planet.home.HomePlanet;

/**
 * 世界银河 管理中心
 * @author deng		
 * @date 2015-6-18 下午4:14:21
 */
public class WorldManager {
	
	private WorldManager(){}
	public static WorldManager o = new WorldManager();
	
	// 所有 母星 列表
	private Map<Short, HomePlanet> homes = new HashMap<Short, HomePlanet>();
	
	// 所有 中转星 列表
	private Map<Short, EntrepotPlanet> entrepots = new HashMap<Short, EntrepotPlanet>();
	
	
	
}
