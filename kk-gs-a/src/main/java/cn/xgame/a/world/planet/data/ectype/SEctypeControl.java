package cn.xgame.a.world.planet.data.ectype;

import java.util.List;

import cn.xgame.config.o.Stars;

import x.javaplus.collections.Lists;

/**
 * 一个星球的副本操作中心
 * @author deng		
 * @date 2015-7-27 下午3:30:00
 */
public class SEctypeControl {

	// 副本本身自带副本
	private List<SEctype> itself = Lists.newArrayList();
	
	// 根据瞭望科技 找到的
	private List<SEctype> outlook = Lists.newArrayList();
	
	/**
	 * 根据表格 初始化 副本列表
	 * @param templet
	 */
	public void fromTemplet( Stars templet ) {
		if( templet.ectypes.isEmpty() )
			return;
		
		String[] content = templet.ectypes.split("\\|");
		for( String x : content ){
			if( x.isEmpty() ) continue;
			String[] v = x.split(";");
			for( String o : v ){
				if( o.isEmpty() ) continue;
				SEctype e = new SEctype( templet.id, Integer.parseInt( o ) );
				itself.add( e );
			}
		}
		
	}

	/**
	 * 获取该星球 所有副本 (包括瞭望到的副本)
	 * @return
	 */
	public List<SEctype> getAll(){
		List<SEctype> ret = Lists.newArrayList();
		ret.addAll(itself);
		ret.addAll(outlook);
		return ret;
	}
	
}
