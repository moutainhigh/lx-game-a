package cn.xgame.a.player.dock;


import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.mysql.db.Condition;
import x.javaplus.util.ErrorCode;

import cn.xgame.a.IFromDB;
import cn.xgame.a.player.dock.capt.CaptainInfo;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.gen.dto.MysqlGen.CaptainsDao;
import cn.xgame.gen.dto.MysqlGen.CaptainsDto;
import cn.xgame.gen.dto.MysqlGen.ShipsDao;
import cn.xgame.gen.dto.MysqlGen.ShipsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.system.SystemCfg;

/**
 * 船坞 操作类
 * @author deng		
 * @date 2015-7-9 下午12:20:41
 */
public class DockControl implements IFromDB{
	
	private Player root;
	
	// 舰船 列表 - 停机坪
	private List<ShipInfo> 		apron 	= Lists.newArrayList();
	
	// 舰长 列表 - 舰长室
	private List<CaptainInfo> 	cabin	= Lists.newArrayList();
	
	
	public DockControl( Player player ){
		this.root = player;
	}
	
	@Override
	public void fromDB() {
		wrapApron();
		wrapCabin();
	}
	private void wrapApron() {
		ShipsDao dao = SqlUtil.getShipsDao();
		String sql = new Condition( ShipsDto.gsidChangeSql( SystemCfg.ID ) ).AND( ShipsDto.unameChangeSql( root.getUID() ) ).toString();
		List<ShipsDto> dtos	= dao.getByExact( sql );
		dao.commit();
		for( ShipsDto dto : dtos ){
			apron.add( new ShipInfo( dto ) );
		}
	}
	private void wrapCabin() {
		CaptainsDao dao = SqlUtil.getCaptainsDao();
		String sql = new Condition( CaptainsDto.gsidChangeSql( SystemCfg.ID ) ).AND( CaptainsDto.unameChangeSql( root.getUID() ) ).toString();
		List<CaptainsDto> dtos = dao.getByExact( sql );
		dao.commit();
		for( CaptainsDto dto : dtos ){
			cabin.add( new CaptainInfo( dto ) );
		}
	}
	/** 保存所有数据到数据库 */
	public void update() {
		for( ShipInfo ship : apron )
			ship.updateDB(root);
		for( CaptainInfo capt : cabin )
			capt.updateDB(root);
	}
	
	////////////////////////////////////////// 舰船  ////////////////////////////////////////////////////////////
	/**
	 * 获取 停机坪 列表
	 * @return
	 */
	public List<ShipInfo> getApron() {
		return apron;
	}
	/**
	 * 根据唯一ID 获取舰船
	 * @param suid
	 * @return
	 */
	public ShipInfo getShip( int suid ) {
		for( ShipInfo o : apron ){
			if( o.getuId() == suid )
				return o;
		}
		return null;
	}
	/**
	 * 根据唯一ID 获取舰船  附带异常
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ShipInfo getShipOfException( int id ) throws Exception {
		ShipInfo ret = getShip( id );
		if( ret == null )
			throw new Exception( ErrorCode.SHIP_NOTEXIST.name() );
		return ret;
	}
	
	/**
	 * 创建一个 舰船
	 * @param snid
	 * @param nid
	 */
	public void createShip( int snid, int nid ) {
		
		ShipInfo ship = new ShipInfo( snid, root.generatorShipUID(), nid );
		
		apron.add( ship );
		
		// 最后在数据库 创建数据
		ship.createDB( root );
	}

	/**
	 * 检测这艘舰船 是否空闲
	 * @param ship
	 * @throws Exception 
	 */
	public boolean isLeisure( ShipInfo ship ) throws Exception {
		// 找出所属舰队
		FleetInfo fleet = root.getFleets().getFleetInfo( ship );
		if( fleet == null )
			return true;
		return fleet.isHover();
	}
	
	/**
	 * 取掉舰长
	 * @param ship
	 * @return
	 */
	public void downCaptain( ShipInfo ship ) {
		CaptainInfo capt = getCaptain( ship.getCaptainUID() );
		if( capt == null ) return;
		capt.setShipUid( -1 );
		ship.setCaptainUID( -1 );
	}
	
	////////////////////////////////////////// 舰长  ////////////////////////////////////////////////////////////
	/**
	 * 获取 舰长室 列表
	 * @return
	 */
	public List<CaptainInfo> getCabin() {
		return cabin;
	}
	/**
	 * 根据唯一ID 获取舰长
	 * @param uid
	 * @return
	 */
	public CaptainInfo getCaptain( int uid ) {
		for( CaptainInfo captain : cabin ){
			if( captain.getuId() == uid )
				return captain;
		}
		return null;
	}
	public CaptainInfo getCaptainOfException( int id ) throws Exception{
		CaptainInfo captain = getCaptain( id );
		if( captain == null )
			throw new Exception( ErrorCode.CAPTAIN_NOTEXIST.name() );
		return captain;
	}
	/**
	 * 创建一个 舰长
	 * @param nid
	 * @param quality 
	 */
	public CaptainInfo createCaptain( int snid, int nid, byte quality ) {
		
		CaptainInfo capt = new CaptainInfo( snid, root.generatorCaptainUID(), nid, quality );
		
		cabin.add( capt );
		
		// 在数据库创建数据
		capt.createDB( root );
		
		return capt;
	}
	
	/**
	 * 销毁一个舰长
	 * @param captain
	 */
	public void destroy( CaptainInfo captain ) {
		if( captain == null ) return;
		remove( captain.getuId() );
		captain.deleteDB( root );
	}
	public void destroy( int uid ) {
		destroy( getCaptain(uid) );
	}
	private void remove(int uid) {
		Iterator<CaptainInfo> iter = cabin.iterator();
		while( iter.hasNext() ){
			if( iter.next().getuId() == uid ){
				iter.remove();
				break;
			}
		}
	}
	
	/**
	 * 结算周薪
	 */
	public void balanceWeekly() {
		for( CaptainInfo captain : cabin ){
			if( captain.isWantPayoff() ){
				root.changeCurrency( -captain.attr().getWeekpay(), "结算舰长周薪" );
			}
		}
	}

}
