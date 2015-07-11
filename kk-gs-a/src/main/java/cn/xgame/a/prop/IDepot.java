package cn.xgame.a.prop;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import x.javaplus.collections.Lists;

/**
 * 仓库
 * @author deng		
 * @date 2015-6-25 下午4:51:20
 */
public class IDepot {

	// 所有道具列表
	protected Map<PropType, List<IProp>> props = new HashMap<PropType, List<IProp>>();

	/**
	 * 获取背包所有道具
	 * @return
	 */
	public List<IProp> getAll(){
		List<IProp> ret = Lists.newArrayList();
		List<IProp> temp = props.get( PropType.STUFF );
		if( temp != null )
			ret.addAll( temp );
		temp = props.get( PropType.CAPTAIN );
		if( temp != null )
			ret.addAll( temp );
		temp = props.get( PropType.SHIP );
		if( temp != null )
			ret.addAll( temp );
		temp = props.get( PropType.CEQUIP );
		if( temp != null )
			ret.addAll( temp );
		temp = props.get( PropType.SEQUIP );
		if( temp != null )
			ret.addAll( temp );
		return ret;
	}
	
	/**
	 * 根据唯一ID 获取道具
	 * @param type
	 * @param uid
	 * @return
	 */
	public IProp getProp( PropType type, int uid ) {
		if( type == null ) return null;
		List<IProp> ls = props.get(type);
		if( ls == null ) return null;
		for( IProp b : ls ){
			if( b.getuId() == uid )
				return b;
		}
		return null;
	}
	public IProp getProp( IProp prop ){
		return getProp( prop.type(), prop.getuId() );
	}
	
	/**
	 * 根据表格ID 获取道具列表
	 * @param nid
	 * @return
	 */
	public List<IProp> getProp( int nid ) {
		List<IProp> ret = Lists.newArrayList();
		List<IProp> ls = getAll();
		for( IProp prop : ls ){
			if( prop.getnId() == nid ){
				ret.add( prop );
			}
		}
		return ret;
	}
	
	/**
	 * 获取一个可以累加的道具
	 * @param type
	 * @param uid
	 * @return
	 */
	public IProp getCanAccProp( PropType type, int nid ) {
		if( type == null ) return null;
		List<IProp> ls = props.get(type);
		if( ls == null ) return null;
		for( IProp b : ls ){
			if( b.getnId() == nid && b.isCanAcc() )
				return b;
		}
		return null;
	}
	public IProp getCanAccProp( IProp prop ){
		return getCanAccProp( prop.type(), prop.getnId() );
	}
	
	/**
	 * 添加一个道具
	 * @param prop
	 */
	public void append( IProp prop ) {
		List<IProp> temp = props.get( prop.type() );
		if( temp == null ){
			temp = Lists.newArrayList();
			props.put( prop.type() , temp );
		}
		temp.add(prop);
	}
	
	/**
	 * 删除一个道具 - 主要是根据道具唯一ID来删除
	 * @param prop
	 */
	public boolean remove( IProp prop ){
		if( prop == null ) return false;
		List<IProp> temp = props.get( prop.type() );
		if( temp == null ) return false;
		
		Iterator<IProp> ls = temp.iterator();
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
