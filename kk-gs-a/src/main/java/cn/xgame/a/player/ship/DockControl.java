package cn.xgame.a.player.ship;

import io.netty.buffer.ByteBuf;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.mysql.db.Condition;

import cn.xgame.a.IFromDB;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.u.Player;
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
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( ships.size() );
		for( ShipInfo ship : ships ){
			ship.buildTransformStream( buffer );
		}
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
		createDB( ship );
	}


	private void append( ShipInfo ship ) {
		ships.add(ship);
	}

	
	
	//TODO---------数据库相关
	
	private void createDB( ShipInfo ship ) {
		ShipsDao dao = SqlUtil.getShipsDao();
		ShipsDto dto = dao.create();
		dto.setGsid( root.getGsid() );
		dto.setUname( root.getUID() );
		dto.setUid( ship.getuId() );
		dto.setNid( ship.getnId() );
		dao.commit(dto);
	}
	
	
	
}
