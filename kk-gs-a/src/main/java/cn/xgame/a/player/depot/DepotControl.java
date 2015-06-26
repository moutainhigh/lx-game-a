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
import cn.xgame.a.prop.stuff.Stuff;
import cn.xgame.a.system.SystemCfg;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.gen.dto.MysqlGen.StuffDao;
import cn.xgame.gen.dto.MysqlGen.StuffDto;

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
		
		StuffDao dao = SqlUtil.getStuffDao();
		// 根据 服务器ID 和 玩家唯一ID 获取
		String sql = new Condition( StuffDto.gsidChangeSql( SystemCfg.ID ) ).AND( StuffDto.unameChangeSql( player.getUID() ) ).toString();
		List<StuffDto> dtos = dao.getByExact( sql );
		
		List<IProp> ret = Lists.newArrayList();
		for( StuffDto o : dtos ){
			Stuff stuff = new Stuff( o );
			ret.add( stuff );
		}
		dao.commit();
		return ret;
	}
	// 舰长
	private List<IProp> fromDBCaptain() {
		
		List<IProp> ret = Lists.newArrayList();
		return ret;
	}
	// 舰船
	private List<IProp> fromDBShip() {
		
		List<IProp> ret = Lists.newArrayList();
		return ret;
	}
	// 舰长-装备
	private List<IProp> fromDBCEquip() {
		List<IProp> ret = Lists.newArrayList();
		return ret;
	}
	// 舰船-装备
	private List<IProp> fromDBSEquip() {
		List<IProp> ret = Lists.newArrayList();
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
