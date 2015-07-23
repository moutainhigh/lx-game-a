package cn.xgame.a.player.depot;

import io.netty.buffer.ByteBuf;

import java.util.List;

import cn.xgame.a.IFromDB;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IDepot;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.a.system.SystemCfg;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Item;
import cn.xgame.gen.dto.MysqlGen.PropsDao;
import cn.xgame.gen.dto.MysqlGen.PropsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.utils.Logs;

import x.javaplus.collections.Lists;
import x.javaplus.mysql.db.Condition;

/**
 * 玩家所有道具 操作中心
 * @author deng		
 * @date 2015-6-17 下午6:56:27
 */
public class DepotControl extends IDepot implements ITransformStream, IFromDB{

	private Player root;
	
	
	public DepotControl( Player player ) {
		this.root = player;
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
		PropsDao dao = SqlUtil.getPropsDao();
		String sql = new Condition( PropsDto.gsidChangeSql( SystemCfg.ID ) ).AND( PropsDto.unameChangeSql( root.getUID() ) ).toString();
		List<PropsDto> dtos = dao.getByExact(sql);
		dao.commit();
		for( PropsDto dto : dtos ){
			IProp prop = wrapInDB( dto );
			append( prop );
		}
	}
	private IProp wrapInDB( PropsDto dto ) {
		Item item = CsvGen.getItem( dto.getNid() );
		return PropType.fromNumber( item.bagtype ).wrapDB( dto );
	}

	/**
	 * 添加道具 并 保存数据库 以及放入背包
	 * @param nid 表格ID
	 * @param count 数量
	 * @return
	 */
	public List<IProp> appendProp( int nid, int count) {
		
		// TODO  以后如果要做玩家的仓库空间限制 就在这里加
		
		List<IProp> ret = Lists.newArrayList();
		Item item 		= CsvGen.getItem(nid);
		if( item == null ){
			Logs.error( root, "创建道具出错 nid="+nid+" 没找到！" );
			return ret;
		}
		
		PropType type 	= PropType.fromNumber( item.bagtype );
		
		IProp prop 		= getCanCumsumProp( nid );
		if( prop == null ){
			ret.add( createProp( type, nid, count ) );
		}else{
			// 算出差值
			int surplus = prop.addCount( count );
			// 保存数据库
			prop.updateDB(root);
			ret.add( prop );
			// 如果有多余的 就在创建一个 
			if( surplus > 0 )
				ret.add( createProp( type, nid, surplus ) );
		}
		return ret;
	}
	public List<IProp> appendProp( IProp prop ) {
		// TODO 以后要加有成长属性的物品   那么就在这里做
		return appendProp( prop.getnId(), prop.getCount() );
	}
	
	/**
	 * 创建一个道具
	 * @param type
	 * @param nid
	 * @param count
	 * @return
	 */
	private IProp createProp( PropType type, int nid, int count){
		// 创建一个道具出来
		IProp prop = type.create( root.generatorPropUID(), nid, count );
		// 放入背包
		append( prop );
		// 在数据库 创建数据
		prop.createDB( root );
		// 这里看数量是否超过累加数
		if( prop.getCount() < count )
			createProp( type, nid, count - prop.getCount() );
		return prop;
	}
	

	/**
	 * 扣除道具
	 * @param clone
	 */
	public void deductProp( IProp clone ) {
		deductProp( clone.getuId(), clone.getCount() );
	}
	public void deductProp( int uid, int count ) {
		
		IProp prop = getProp(uid);
		if( prop == null ) {
			Logs.error( root, "prop == null at DepotControl.deductProp( int uid, int count )" );
			return;
		}
		
		// 执行扣除
		prop.deductCount( count );
		if( prop.isEmpty() )
			remove( prop );
		else
			prop.updateDB(root);
		
		Logs.debug( root, "扣除道具 ("+uid+","+count+"), 扣除后 (" + prop + ")" );
	}
	
	public boolean remove( IProp prop ){
		// 从数据库删除
		prop.deleteDB(root);
		// 然后在从内存中删除
		return super.remove(prop);
	}

	/** 保存数据库 */
	public void update() {
		for( IProp prop : props )
			prop.updateDB(root);
	}


	
}
