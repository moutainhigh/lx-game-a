package cn.xgame.a.player.prop;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xgame.a.player.ITransformStream;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.prop.stuff.Stuff;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.system.SystemCfg;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.gen.dto.MysqlGen.StuffDao;
import cn.xgame.gen.dto.MysqlGen.StuffDto;

import x.javaplus.collections.Lists;
import x.javaplus.mysql.db.Condition;

/**
 * 背包操作中心
 * @author deng		
 * @date 2015-6-17 下午6:56:27
 */
public class PropControl implements ITransformStream{

	private Player player;
	
	// 背包列表
	private Map<PropType, List<IProp>> bags = new HashMap<PropType, List<IProp>>();
	
	public PropControl( Player player ) {
		this.player = player;
	}
	
	/**
	 * 获取背包所有道具
	 * @return
	 */
	public List<IProp> getAll(){
		List<IProp> ret = Lists.newArrayList();
		ret.addAll( bags.get( PropType.STUFF ) );
		ret.addAll( bags.get( PropType.CAPTAIN ) );
		ret.addAll( bags.get( PropType.SHIP ) );
		ret.addAll( bags.get( PropType.CEQUIP ) );
		ret.addAll( bags.get( PropType.SEQUIP ) );
		return ret;
	}
	
	/**
	 * 只 塞入 基础数据
	 */
	public void buildTransformStream( ByteBuf response ) {
		List<IProp> ls = getAll();
		response.writeShort( ls.size() );
		for( IProp bag : ls ){
			bag.putBaseBuffer( response );
		}
	}
	

	/** 从DB获取数据  */
	public void fromDB() {
		bags.clear();
		// 材料
		bags.put( PropType.STUFF, DBFromStuff() );
		// 舰长
		bags.put( PropType.CAPTAIN, DBFromCaptain() );
		// 舰船
		bags.put( PropType.SHIP, DBFromShip() );
		// 舰长-装备
		bags.put( PropType.CEQUIP, DBFromCEquip() );
		// 舰船-装备
		bags.put( PropType.SEQUIP, DBFromSEquip() );
	}
	
	
	// 材料
	private List<IProp> DBFromStuff() {
		
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
	private List<IProp> DBFromCaptain() {
		
		List<IProp> ret = Lists.newArrayList();
		return ret;
	}
	// 舰船
	private List<IProp> DBFromShip() {
		
		List<IProp> ret = Lists.newArrayList();
		return ret;
	}
	// 舰长-装备
	private List<IProp> DBFromCEquip() {
		List<IProp> ret = Lists.newArrayList();
		return ret;
	}
	// 舰船-装备
	private List<IProp> DBFromSEquip() {
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
		IProp prop = type.create( 2, nid, count );
		// 在数据库 创建数据
		prop.createDB( player );
		return prop;
	}
	
	/**
	 * 获取道具
	 * @param type
	 * @param uid
	 * @return
	 */
	private IProp getProp( PropType type, int uid ) {
		List<IProp> ls = bags.get(type);
		for( IProp b : ls ){
			if( b.getuId() == uid )
				return b;
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		Player p = PlayerManager.o.getPlayer( "101" );
		
//		BagControl bag = new BagControl(p);
//		
//		Bag b = bag.createProp( PropType.STUFF, 101, 20 );
		
//		b.getProp().setCount( 30 );
		
//		b.getProp().updateDB(p);
		
		for( IProp b : p.getBags().getAll() ){
			System.out.println( b.toString() );
		}
		
		IProp bag = p.getBags().getProp( PropType.STUFF, 2 );
		bag.setCount( 27 );
		bag.updateDB( p );
		
		for( IProp b : p.getBags().getAll() ){
			System.out.println( b.toString() );
		}
	}






	
}
