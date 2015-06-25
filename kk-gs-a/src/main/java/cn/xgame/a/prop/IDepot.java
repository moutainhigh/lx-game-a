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
	protected Map<PropType, List<IProp>> props = new HashMap<PropType, List<IProp>>();
	
	
	/**
	 * 获取背包所有道具
	 * @return
	 */
	public List<IProp> getAll(){
		List<IProp> ret = Lists.newArrayList();
		ret.addAll( props.get( PropType.STUFF ) );
		ret.addAll( props.get( PropType.CAPTAIN ) );
		ret.addAll( props.get( PropType.SHIP ) );
		ret.addAll( props.get( PropType.CEQUIP ) );
		ret.addAll( props.get( PropType.SEQUIP ) );
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
