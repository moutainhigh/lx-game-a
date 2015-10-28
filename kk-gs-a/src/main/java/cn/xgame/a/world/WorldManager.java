package cn.xgame.a.world;

import io.netty.buffer.ByteBuf;

import java.util.List;


import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.a.world.planet.ectype.EctypePlanet;
import cn.xgame.a.world.planet.entrepot.EntrepotPlanet;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.StarsPo;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDao;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

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
		
		for( StarsPo star : CsvGen.starspos ){
			
			try {
				if( star.type == 1 ){
					
					HomePlanet home = new HomePlanet( star );
					assign( home );
					homes.add( home );
				}else if( star.type == 2 ){
					
					EntrepotPlanet entrepot = new EntrepotPlanet(star);
					assign( entrepot );
					entrepots.add( entrepot );
				}else if( star.type == 3 ){
					
					EctypePlanet ectype = new EctypePlanet(star);
					assign( ectype );
					ectypes.add( ectype );
				}
			} catch (Exception e) {
				Logs.error( "初始化星图错误 at="+star.id, e );
			}
		}
		
	}
	private void assign( IPlanet planet ) {
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
	 * 获取玩家所属母星
	 * @param player
	 * @return
	 * @throws Exception 
	 */
	public HomePlanet getHPlanetInPlayer( Player player ) throws Exception {
		return getHomePlanet( player.getCountryId() );
	}

	/**
	 * 获取母星 根据星球ID
	 * @param nid
	 * @return
	 * @throws Exception 
	 */
	public HomePlanet getHomePlanet( int nid ) throws Exception {
		for( HomePlanet home : homes ){
			if( home.getId() == nid )
				return home;
		}
		throw new Exception( ErrorCode.PLANET_NOTEXIST.name() );
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
	 * @throws Exception 
	 */
	public IPlanet getPlanet( int nid ) throws Exception {
		StarsPo star = CsvGen.getStarsPo( nid );
		if( star == null )
			throw new Exception( ErrorCode.PLANET_NOTEXIST.name() );
		if( star.type == 1 ){
			return getHomePlanet( nid );
		}else if( star.type == 2 ){
			return getEntrepotPlanet( nid );
		}else if( star.type == 3 ){
			return getEctypePlanet( nid );
		}
		throw new Exception( ErrorCode.PLANET_NOTEXIST.name() );
	}

	public List<IPlanet> getAllPlanet(){
		List<IPlanet> ret = Lists.newArrayList();
		ret.addAll(homes);
		ret.addAll(entrepots);
		ret.addAll(ectypes);
		return ret;
	}
	public List<HomePlanet> getAllHomePlanet() {
		return homes;
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
	 * 刷新每个星球的副本数据
	 */
	public void updateGeneralEctype() {
		List<IPlanet> ls = getAllPlanet();
		for( IPlanet o : ls ){
			o.initChapters();
		}
	}
	
	/**
	 * 根据瞭望距离获取周围的星球
	 * @param qutlook
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public List<Integer> getDistanceConfineTo( int qutlook, int x, int y, int z ) {
		List<Integer> ret = Lists.newArrayList();
		List<IPlanet> ls = getAllPlanet();
		for( IPlanet planet : ls ){
			Lua lua = LuaUtil.getEctypeCombat();
			LuaValue[] value = lua.getField("conversionDistance").call( 1, planet.templet().x, planet.templet().y, planet.templet().z, x, y, z );
			if( value[0].getInt() <= qutlook )
				ret.add(planet.getId());
		}
		return ret;
	}
	

}
