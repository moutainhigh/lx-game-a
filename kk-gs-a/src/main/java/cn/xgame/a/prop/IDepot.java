package cn.xgame.a.prop;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;

/**
 * 仓库基类
 * @author deng		
 * @date 2015-6-25 下午4:51:20
 */
public class IDepot {

	// 所有道具列表
	protected List<IProp> props = Lists.newArrayList();

	/**
	 * 获取背包所有道具
	 * @return
	 */
	public List<IProp> getAll(){
		return props;
	}
	
	/**
	 * 获取全部道具的占用空间
	 * @return
	 */
	public short getAllOccupyRoom(){
		short ret = 0;
		for( IProp o : props )
			ret += o.occupyRoom();
		return ret;
	}
	
	/**
	 * 根据唯一ID 获取道具
	 * @param uid
	 * @return
	 */
	public IProp getProp( int uid ){
		for( IProp o : props ){
			if( o.getuId() == uid )
				return o;
		}
		return null;
	}
	public IProp getProp( IProp prop ){
		return getProp( prop.getuId() );
	}
	
	/**
	 * 根据表格ID 获取道具列表
	 * @param nid
	 * @return
	 */
	public List<IProp> getPropListInNid( int nid ) {
		List<IProp> ret = Lists.newArrayList();
		for( IProp prop : props ){
			if( prop.getnId() == nid ){
				ret.add( prop );
			}
		}
		return ret;
	}
	
	/**
	 * 获取一个可以累加的道具
	 * @param nid 表格ID
	 * @return
	 */
	public IProp getCanCumsumProp( int nid ){
		for( IProp o : props ){
			if( o.getnId() == nid && o.isCanCumsum() )
				return o;
		}
		return null;
	}
	public IProp getCanCumsumProp( IProp prop ){
		return getCanCumsumProp( prop.getnId() );
	}
	
	/**
	 * 添加一个道具
	 * @param prop
	 */
	protected void append( IProp prop ) {
		if( getProp( prop ) != null )
			return;
		props.add(prop);
	}
	
	/**
	 * 删除一个道具 - 主要是根据道具唯一ID来删除
	 * @param prop
	 */
	public boolean remove( IProp prop ){
		if( prop == null ) return false;
		Iterator<IProp> ls = props.iterator();
		while( ls.hasNext() ){
			IProp next = ls.next();
			if( next.getuId() == prop.getuId() ){
				ls.remove();
				return true;
			}
		}
		return false;
	}
	
}
