package cn.xgame.a.prop;

import java.util.HashMap;
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
	protected Map<PropType, List<IProp>> props ;
	
	public IDepot(){
		props = new HashMap<PropType, List<IProp>>();
//		List<IProp> ret = Lists.newArrayList();
//		for( )
	}
	
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
		temp = props.get( PropType.CEQUIP );
		if( temp != null )
			ret.addAll( temp );
		return ret;
	}
	
	/**
	 * 获取道具
	 * @param type
	 * @param uid
	 * @return
	 */
	public IProp getProp( PropType type, int uid ) {
		List<IProp> ls = props.get(type);
		for( IProp b : ls ){
			if( b.getuId() == uid )
				return b;
		}
		return null;
	}
	
}
