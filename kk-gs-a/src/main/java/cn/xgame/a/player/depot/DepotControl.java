package cn.xgame.a.player.depot;

import io.netty.buffer.ByteBuf;

import java.util.List;

import cn.xgame.a.IFromDB;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IDepot;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.a.prop.captain.Captains;
import cn.xgame.a.prop.cequip.CEquip;
import cn.xgame.a.prop.sequip.SEquip;
import cn.xgame.a.prop.ship.Ships;
import cn.xgame.a.prop.stuff.Stuff;
import cn.xgame.a.system.SystemCfg;
import cn.xgame.gen.dto.MysqlGen.M_captainDao;
import cn.xgame.gen.dto.MysqlGen.M_captainDto;
import cn.xgame.gen.dto.MysqlGen.M_cequipDao;
import cn.xgame.gen.dto.MysqlGen.M_cequipDto;
import cn.xgame.gen.dto.MysqlGen.M_sequipDao;
import cn.xgame.gen.dto.MysqlGen.M_sequipDto;
import cn.xgame.gen.dto.MysqlGen.M_shipDao;
import cn.xgame.gen.dto.MysqlGen.M_shipDto;
import cn.xgame.gen.dto.MysqlGen.M_stuffDao;
import cn.xgame.gen.dto.MysqlGen.M_stuffDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;

import x.javaplus.collections.Lists;
import x.javaplus.mysql.db.Condition;

/**
 * 玩家所有道具 操作中心
 * @author deng		
 * @date 2015-6-17 下午6:56:27
 */
public class DepotControl extends IDepot implements ITransformStream, IFromDB{

	private Player player;
	
	
	public DepotControl( Player player ) {
		this.player = player;
	}
	
	
	/**
	 * 只 塞入 基础数据
	 */
	@Override
	public void buildTransformStream( ByteBuf response ) {
		List<IProp> ls = getAll();
		response.writeShort( ls.size() );
		for( IProp bag : ls ){
			bag.putBaseBuffer( response );
		}
	}
	
	@Override
	public void fromDB() {
		props.clear();
		// 材料
		props.put( PropType.STUFF, fromDBStuff() );
		// 舰长
		props.put( PropType.CAPTAIN, fromDBCaptain() );
		// 舰船
		props.put( PropType.SHIP, fromDBShip() );
		// 舰长-装备
		props.put( PropType.CEQUIP, fromDBCEquip() );
		// 舰船-装备
		props.put( PropType.SEQUIP, fromDBSEquip() );
	}
	
	
	// 材料
	private List<IProp> fromDBStuff() {
		M_stuffDao dao = SqlUtil.getM_stuffDao();
		// 根据 服务器ID 和 玩家唯一ID 获取
		String sql = new Condition( M_stuffDto.gsidChangeSql( SystemCfg.ID ) ).AND( M_stuffDto.unameChangeSql( player.getUID() ) ).toString();
		List<M_stuffDto> dtos = dao.getByExact( sql );
		List<IProp> ret = Lists.newArrayList();
		for( M_stuffDto o : dtos ){
			Stuff stuff = Stuff.wrapDB( o );
			ret.add( stuff );
		}
		dao.commit();
		return ret;
	}
	// 舰长
	private List<IProp> fromDBCaptain() {
		M_captainDao dao = SqlUtil.getM_captainDao();
		String sql = new Condition( M_captainDto.gsidChangeSql( SystemCfg.ID ) ).AND( M_captainDto.unameChangeSql( player.getUID() ) ).toString();
		List<M_captainDto> dtos = dao.getByExact( sql );
		List<IProp> ret = Lists.newArrayList();
		for( M_captainDto o : dtos ){
			Captains x = Captains.wrapDB(o);
			ret.add(x);
		}
		return ret;
	}
	// 舰船
	private List<IProp> fromDBShip() {
		M_shipDao dao = SqlUtil.getM_shipDao();
		String sql = new Condition( M_shipDto.gsidChangeSql( SystemCfg.ID ) ).AND( M_shipDto.unameChangeSql( player.getUID() ) ).toString();
		List<M_shipDto> dtos = dao.getByExact( sql );
		List<IProp> ret = Lists.newArrayList();
		for( M_shipDto o : dtos ){
			Ships x = Ships.wrapDB(o);
			ret.add( x );
		}
		return ret;
	}
	// 舰长-装备
	private List<IProp> fromDBCEquip() {
		M_cequipDao dao = SqlUtil.getM_cequipDao();
		String sql = new Condition( M_cequipDto.gsidChangeSql( SystemCfg.ID ) ).AND( M_cequipDto.unameChangeSql( player.getUID() ) ).toString();
		List<M_cequipDto> dtos = dao.getByExact( sql );
		List<IProp> ret = Lists.newArrayList();
		for( M_cequipDto o : dtos ){
			CEquip x = CEquip.wrapDB(o);
			ret.add( x );
		}
		return ret;
	}
	// 舰船-装备
	private List<IProp> fromDBSEquip() {
		M_sequipDao dao = SqlUtil.getM_sequipDao();
		String sql = new Condition( M_sequipDto.gsidChangeSql( SystemCfg.ID ) ).AND( M_sequipDto.unameChangeSql( player.getUID() ) ).toString();
		List<M_sequipDto> dtos = dao.getByExact( sql );
		List<IProp> ret = Lists.newArrayList();
		for( M_sequipDto o : dtos ){
			SEquip x = SEquip.wrapDB(o);
			ret.add( x );
		}
		return ret;
	}
	
	/**
	 * 创建一个道具 并 保存数据库 以及放入背包
	 * @param type
	 * @param nid 表格ID
	 * @param count 数量
	 * @return
	 */
	public IProp createProp( PropType type, int nid, int count) {
		// 创建一个道具出来
		IProp prop = type.create( player.generatorPropUID(type), nid, count );
		// 放入背包
		append( prop );
		// 在数据库 创建数据
		prop.createDB( player );
		return prop;
	}
	

	

	public static void main(String[] args) {
		
		Player p = PlayerManager.o.getPlayer( "101", SystemCfg.ID );
		
//		p.getProps().createProp( PropType.STUFF, 101, 20 );
//		p.getProps().createProp( PropType.CAPTAIN, 201, 1 );
		
		for( IProp b : p.getProps().getAll() ){
			System.out.println( b.toString() );
		}
//		
//		IProp bag = p.getProps().getProp( PropType.STUFF, 2 );
//		bag.setCount( 27 );
//		bag.updateDB( p );
//		
//		for( IProp b : p.getProps().getAll() ){
//			System.out.println( b.toString() );
//		}
	}






	
}
