package cn.xgame.a.world.planet;

import cn.xgame.config.o.Stars;

/**
 * 星球基类
 * @author deng		
 * @date 2015-6-25 下午4:32:24
 */
public class IPlanet {

	// 星球配置表
	protected Stars templet;
	
	// 星球总空间
	protected short maxSpace;
	
	// 星球仓库
	protected DepotControl depots;
	
	// 星球建筑
	protected BuildingControl buildings;
	
}
