package cn.xgame.gen.o;

/**
 *  星球数据
 * @author deng		
 * @date 2015-6-26 上午10:02:40
 */
public class PlanetData {
	
	/** 星球ID */
	int id;
	
	/** 星球总空间 */
	short maxSpace;
	
	/** 玩家列表 */
	byte[] players;
	
	/** 驱逐元老列表 */
	byte[] expelGenr;
	
	/** 星球建筑 */
	byte[] buildings;
	
	/** 星球仓库 */
	byte[] depots;
	
	/** 星球特产 */
	byte[] specialtys;
	
	/** 星球科技 */
	byte[] techs;
}
