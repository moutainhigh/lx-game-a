package cn.xgame.a.player.ship;

import io.netty.buffer.ByteBuf;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.mysql.db.Condition;
import x.javaplus.util.ErrorCode;

import cn.xgame.a.IFromDB;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.captain.o.CaptainInfo;
import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.system.SystemCfg;
import cn.xgame.gen.dto.MysqlGen.ShipsDao;
import cn.xgame.gen.dto.MysqlGen.ShipsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;

/**
 * 船坞 操作类
 * @author deng		
 * @date 2015-7-9 下午12:20:41
 */
public class DockControl implements ITransformStream,IFromDB{
	
	private Player root;
	
	// 舰船 列表
	private List<ShipInfo> ships = Lists.newArrayList();
	
	public DockControl( Player player ){
		this.root = player;
	}

	@Override
	public void fromDB() {
		ships.clear();
		ShipsDao dao = SqlUtil.getShipsDao();
		String sql = new Condition( ShipsDto.gsidChangeSql( SystemCfg.ID ) ).AND( ShipsDto.unameChangeSql( root.getUID() ) ).toString();
		List<ShipsDto> dtos	= dao.getByExact( sql );
		dao.commit();
		for( ShipsDto dto : dtos ){
			ShipInfo ship = new ShipInfo( dto );
			ships.add(ship);
		}
	}
	
	/** 保存所有数据到数据库 */
	public void update() {
		for( ShipInfo ship : ships )
			ship.updateDB(root);
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( ships.size() );
		for( ShipInfo ship : ships ){
			ship.buildTransformStream( buffer );
			CaptainInfo cap = root.getCaptains().getCaptain( ship.getCaptainUID() );
			buffer.writeInt( cap == null ? -1 : cap.getnId() );
			buffer.writeInt( ship.getStarId() );
			ship.getHolds().buildTransformStream(buffer);
			ship.getWeapons().buildTransformStream(buffer);
			ship.getAssists().buildTransformStream(buffer);
		}
	}

	public List<ShipInfo> getAllShip() {
		return ships;
	}
	
	/**
	 * 装备一个舰船
	 * @param ship
	 */
	public void equipShip( ShipInfo ship ) {
		append( ship );
	}

	/**
	 * 创建一个 舰船 并装备上
	 * @param nid
	 */
	public void createShip( int nid ) {
		ShipInfo ship = new ShipInfo( root.generatorShipUID(), nid );
		append( ship );
		
		// 最后在数据库 创建数据
		ship.createDB( root );
	}
	private void append( ShipInfo ship ) {
		// 默认设置停靠在自己母星上
		ship.setStarId( root.getCountryId() );
		ships.add(ship);
	}

	/**
	 * 根据唯一ID 获取舰船
	 * @param suid
	 * @return
	 */
	public ShipInfo getShip( int suid ) {
		for( ShipInfo o : ships ){
			if( o.getuId() == suid )
				return o;
		}
		return null;
	}

	//TODO-------------其他函数

	/**
	 * 放一个道具到 舰船货仓
	 * @param suid 
	 * @param clone
	 * @return
	 * @throws Exception 
	 */
	public List<IProp> putinHold( int suid, IProp clone ) throws Exception {
		// 检查舰船是否存在
		ShipInfo ship = getShip(suid);
		if( ship == null )
			throw new Exception( ErrorCode.SHIP_NOTEXIST.name() ) ;
		
		// 看货仓是否 还有空间
		if( ship.getHolds().roomIsEnough( clone ) )
			throw new Exception( ErrorCode.ROOM_LAZYWEIGHT.name() );
		
		// 放入货仓
		List<IProp> ret = ship.getHolds().appendProp( clone );
		
		// 最后保存数据库
		ship.updateDB(root);
		
		return ret;
	}

	/**
	 * 在舰船货仓里面卸下一个道具
	 * @param suid
	 * @param uid
	 * @param count
	 * @return
	 * @throws Exception 
	 */
	public IProp unloadHoldProp( int suid, int uid, int count ) throws Exception {
		// 检查舰船是否存在
		ShipInfo ship = getShip(suid);
		if( ship == null )
			throw new Exception( ErrorCode.SHIP_NOTEXIST.name() ) ;
		// 执行扣除
		IProp ret = ship.getHolds().deductProp( uid, count );
		if( ret == null )
			throw new Exception( ErrorCode.PROP_NOTEXIST.name() ) ;
		// 最后保存一下数据库
		ship.updateDB(root);
		return ret;
	}



	
	
}
