package cn.xgame.a.world;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.a.world.planet.ectype.EctypePlanet;
import cn.xgame.a.world.planet.entrepot.EntrepotPlanet;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Stars;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDao;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.utils.Logs;

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
	
	// 所有 副本星 列表
	private List<EctypePlanet> ectypes = Lists.newArrayList();
	
	/** 初始化 星图 */
	public void initialize(){
		
		for( Stars star : CsvGen.starss ){
			
			try {
				if( star.tpye == 1 ){
					
					HomePlanet home = new HomePlanet( star );
					append( home );
					homes.add( home );
				}else if( star.tpye == 2 ){
					
					EntrepotPlanet entrepot = new EntrepotPlanet(star);
					append( entrepot );
					entrepots.add( entrepot );
				}else if( star.tpye == 3 ){
					
					EctypePlanet ectype = new EctypePlanet(star);
					append( ectype );
					ectypes.add( ectype );
				}
			} catch (Exception e) {
				Logs.error( "初始化星图错误 at="+star.id, e );
			}
		}
		
	}

	private void append( IPlanet planet ) {
		PlanetDataDao dao = SqlUtil.getPlanetDataDao();
		PlanetDataDto dto = dao.get( planet.getId() );
		if( dto == null ){
			dto = dao.create();
			planet.init( dto );
			dao.commit( dto );
		}else{
			planet.wrap( dto );
			dao.commit();
		}
	}


	/**
	 * 分配母星
	 * @param player
	 */
	public HomePlanet allotHomePlanet( Player player ){
		
		// 先根据ip获取对应母星
		HomePlanet home = getHomeInIP( player.getIp() );
		
		// 把玩家放入 母星
		home.add( player );
		player.setCountryId( home.getId() );// 这里对应设置 是为了 方便查找
		
		// 初始化 领地
//		player.
		return home;
	}
	
	// 根据ip获取 对应母星
	private HomePlanet getHomeInIP(String ip) {
		return homes.get(0);
	}


	/**
	 * 获取玩家所属母星
	 * @param player
	 * @return
	 */
	public HomePlanet getHPlanetInPlayer( Player player ) {
		for( HomePlanet home : homes ){
			if( home.getId() == player.getCountryId() )
				return home;
		}
		return null;
	}
	


	
}
