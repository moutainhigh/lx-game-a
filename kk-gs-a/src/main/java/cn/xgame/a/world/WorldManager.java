package cn.xgame.a.world;

import io.netty.buffer.ByteBuf;

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
				if( star.type == 1 ){
					
					HomePlanet home = new HomePlanet( star );
					append( home );
					homes.add( home );
				}else if( star.type == 2 ){
					
					EntrepotPlanet entrepot = new EntrepotPlanet(star);
					append( entrepot );
					entrepots.add( entrepot );
				}else if( star.type == 3 ){
					
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
		HomePlanet home = getHomePlanetInIP( player.getIp() );
		
		// 把玩家放入 母星
		home.appendPlayer( player );
		player.setCountryId( home.getId() );// 这里对应设置 是为了 方便查找
		
		// 这里及时存下
		home.updateDB();
		return home;
	}
	
	// 根据ip获取 对应母星
	private HomePlanet getHomePlanetInIP( String ip ) {
		return homes.get(0);
	}
	
	/**
	 * 获取玩家所属母星
	 * @param player
	 * @return
	 */
	public HomePlanet getHPlanetInPlayer( Player player ) {
		return getHomePlanet( player.getCountryId() );
	}

	/**
	 * 获取母星 根据星球ID
	 * @param nid
	 * @return
	 */
	public HomePlanet getHomePlanet( int nid ) {
		for( HomePlanet home : homes ){
			if( home.getId() == nid )
				return home;
		}
		return null;
	}

	/**
	 * 获取中转星 根据星球ID
	 * @param nid
	 * @return
	 */
	public EntrepotPlanet getEntrepotPlanet( int nid ) {
		for( EntrepotPlanet entrepot : entrepots ){
			if( entrepot.getId() == nid )
				return entrepot;
		}
		return null;
	}
	
	/**
	 * 获取副本星 根据星球ID
	 * @param nid
	 * @return
	 */
	public EctypePlanet getEctypePlanet( int nid ) {
		for( EctypePlanet ectype : ectypes ){
			if( ectype.getId() == nid )
				return ectype;
		}
		return null;
	}
	
	/**
	 * 获取星球 - 根据表格ID
	 * @param nid
	 * @return
	 */
	public IPlanet getPlanet( int nid ) {
		Stars star = CsvGen.getStars( nid );
		if( star == null )
			return null;
		if( star.type == 1 ){
			return getHomePlanet( nid );
		}else if( star.type == 2 ){
			return getEntrepotPlanet( nid );
		}else if( star.type == 3 ){
			return getEctypePlanet( nid );
		}
		return null;
	}

	public List<IPlanet> getAllPlanet(){
		List<IPlanet> ret = Lists.newArrayList();
		ret.addAll(homes);
		ret.addAll(entrepots);
		ret.addAll(ectypes);
		return ret;
	}

	/**
	 * 塞入星图
	 * @param response
	 */
	public void putStaratlas( ByteBuf response ) {
		List<IPlanet> ls = getAllPlanet();
		response.writeByte( ls.size() );
		for( IPlanet planet : ls ){
			response.writeInt( planet.getId() );
			response.writeByte( planet.getInstitution() == null ? 0 : planet.getInstitution().toNumber() );
			response.writeInt( planet.getPeople() );
			response.writeByte( planet.getTechLevel() );
		}
	}
	
	/** 线程 */
	public void run() {
		for( HomePlanet home : homes ){
			// 在这里 处理 特产线程
			home.run();
			// 在这里保存一下 星球数据 以后所有的改动都不需要及时保存 就在这里保存就够了
			home.updateDB();
		}
		
		for( EctypePlanet ectype : ectypes ){
			
			// 在这里保存一下 星球数据 以后所有的改动都不需要及时保存 就在这里保存就够了
			ectype.updateDB();
		}
		
		for( EntrepotPlanet entrepot : entrepots ){
			
			// 在这里保存一下 星球数据 以后所有的改动都不需要及时保存 就在这里保存就够了
			entrepot.updateDB();
		}
	}
	
	/**
	 * 执行 研发线程 - 包括建筑建造 和 科技研究
	 */
	public void runDevelopment() {
		for( HomePlanet home : homes ){
			home.runDevelopment();
		}
	}
	
	/**
	 * 体制更新 线程
	 */
	public void runUpdateInstitution(){
		for( HomePlanet home : homes ){
			home.updateInstitution();
		}
	}
	
	/**
	 * 酒馆更新
	 */
	public void runTavern() {
		for( HomePlanet home : homes ){
			home.updateTavern();
		}
	}

}
