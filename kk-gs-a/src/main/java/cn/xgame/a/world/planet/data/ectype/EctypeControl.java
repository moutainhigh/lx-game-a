package cn.xgame.a.world.planet.data.ectype;

import java.util.List;

import x.javaplus.collections.Lists;

/**
 * 副本操作中心
 * @author deng		
 * @date 2015-7-13 下午6:16:16
 */
public class EctypeControl {

	// 普通单人副本
	private List<EctypeLists> normalTiwn = Lists.newArrayList();
	
	// 普通多人副本
	private List<EctypeLists> normalMore = Lists.newArrayList();
	
	/** 根据表格初始化副本 */
	public void fromTemplet( String content ) {
		if( content == null || content.isEmpty() )
			return;
		String[] ls = content.split(";");
		for( String x : ls ){
			if( x.isEmpty() ) continue;
			EctypeLists el = new EctypeLists( Integer.parseInt(x) );
			normalTiwn.add(el);
		}
	}
	
	/**
	 * 获取所有副本
	 * @return
	 */
	public List<EctypeLists> getAllEctype() {
		List<EctypeLists> ret = Lists.newArrayList();
		ret.addAll(normalTiwn);
		ret.addAll(normalMore);
		return ret;
	}


}
