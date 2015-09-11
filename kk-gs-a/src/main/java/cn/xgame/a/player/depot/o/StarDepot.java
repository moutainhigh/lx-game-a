package cn.xgame.a.player.depot.o;

import io.netty.buffer.ByteBuf;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.mysql.db.Condition;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IDepot;
import cn.xgame.a.prop.IProp;
import cn.xgame.gen.dto.MysqlGen.PropsDao;
import cn.xgame.gen.dto.MysqlGen.PropsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.system.LXConstants;
import cn.xgame.utils.Logs;

/**
 * 玩家在星球的仓库
 * @author deng		
 * @date 2015-9-7 下午4:16:45
 */
public class StarDepot extends IDepot implements ITransformStream{
	
	private final Player root;

	// 所属星球ID
	private final int beSnid;
	
	public StarDepot( Player root, int beSnid ) {
		this.root = root;
		this.beSnid = beSnid;
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeShort( props.size() );
		for( IProp o : props ){
			o.putBaseBuffer(buffer);
			o.buildTransformStream(buffer);
		}
	}
	
	public int getBeSnid() { return beSnid; 	}

	/**
	 * 添加道具 并 保存数据库 以及放入背包
	 * @param nid 表格ID
	 * @param count 数量
	 * @return
	 */
	public List<IProp> appendProp( int nid, int count ) {
		List<IProp> ret = Lists.newArrayList();
		
		// 检测是否货币
		if( nid == LXConstants.CURRENCY_NID ){
			root.changeCurrency( count );
			ret.add( IProp.create( 0, nid, count) );
			return ret;
		}
		
		// TODO  以后如果要做玩家的仓库空间限制 就在这里加
		
		try {
			IProp prop = getCanCumsumProp( nid );
			if( prop == null ){
				ret.add( createProp( nid, count ) );
			}else{
				// 算出差值
				int surplus = prop.addCount( count );
				// 保存数据库
				updateDB( prop );
				ret.add( prop );
				// 如果有多余的 就在创建一个 
				if( surplus > 0 )
					ret.add( createProp( nid, surplus ) );
			}
		} catch (Exception e) {
			Logs.debug( root, e.getMessage() );
		}
		
		return ret;
	}
	public List<IProp> appendProp( IProp prop ) {
		if( prop.getMaxOverlap() == 1 ){
			zappend( prop );
			return Lists.newArrayList( prop );
		}
		return appendProp( prop.getNid(), prop.getCount() );
	}
	
	/**
	 * 添加一个原样道具
	 * @param root
	 * @param prop
	 */
	private void zappend( IProp prop ) {
		// 设置唯一ID
		prop.setUid( root.generatorPropUID() );
		// 放入背包
		super.append( prop );
		// 在数据库 创建数据
		createDB( prop );
	}
	
	/**
	 * 创建一个道具
	 * @param type
	 * @param nid
	 * @param count
	 * @return
	 * @throws Exception 
	 */
	private IProp createProp( int nid, int count ) throws Exception{
		// 创建一个道具出来
		IProp prop = IProp.create( root.generatorPropUID(), nid, count );
		if( prop == null )
			throw new Exception( "创建道具出错 nid="+nid+" 没找到！" );
		prop.randomAttachAttr();
		// 放入背包
		super.append( prop );
		// 在数据库 创建数据
		createDB( prop );
		// 这里看数量是否超过累加数
		if( prop.getCount() < count )
			createProp( nid, count - prop.getCount() );
		return prop;
	}
	

	/**
	 * 扣除道具
	 * @param clone
	 */
	public void deductProp( IProp clone ) {
		deductProp( clone.getUid(), clone.getCount() );
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
			updateDB( prop );
		
		Logs.debug( root, "扣除道具 ("+uid+","+count+"), 扣除后 (" + prop + ")" );
	}
	
	public boolean remove( IProp prop ){
		// 从数据库删除
		deleteDB( prop );
		// 然后在从内存中删除
		return super.remove(prop);
	}

	/**
	 * 在数据库创建数据
	 * @param prop
	 */
	public void createDB( IProp prop ){
		PropsDao dao = SqlUtil.getPropsDao();
		PropsDto dto = dao.create();
		dto.setGsid( root.getGsid() );
		dto.setUname( root.getUID() );
		dto.setBeSnid( beSnid );
		dto.setUid( prop.getUid() );
		dto.setNid( prop.getNid() );
		dto.setCount( prop.getCount() );
		dto.setQuality( prop.getQuality().toNumber() );
		dto.setAttach( prop.toAttachBytes() );
		dao.commit(dto);
	}
	
	/**
	 * 更新数据库数据 - 只用于玩家仓库
	 * @param prop  
	 * @param root
	 */
	public void updateDB( IProp prop ) {
		PropsDao dao 	= SqlUtil.getPropsDao();
		String sql 		= new Condition( PropsDto.uidChangeSql( prop.getUid() ) ).AND( PropsDto.gsidChangeSql( root.getGsid() ) ).
				AND( PropsDto.unameChangeSql( root.getUID() ) ).toString();
		PropsDto dto	= dao.updateByExact( sql );
		dto.setNid( prop.getNid() );
		dto.setCount( prop.getCount() );
		dto.setQuality( prop.getQuality().toNumber() );
		dto.setAttach( prop.toAttachBytes() );
		dao.commit(dto);
	}

	/**
	 * 从数据库删除数据 - 只用于玩家仓库
	 * @param prop
	 */
	public void deleteDB( IProp prop ){
		PropsDao dao 	= SqlUtil.getPropsDao();
		String sql 		= new Condition( PropsDto.uidChangeSql( prop.getUid() ) ).AND( PropsDto.gsidChangeSql( root.getGsid() ) ).
				AND( PropsDto.unameChangeSql( root.getUID() ) ).toString();
		dao.deleteByExact(sql);
		dao.commit();
	}


}
