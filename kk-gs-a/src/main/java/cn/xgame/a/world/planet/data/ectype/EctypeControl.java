package cn.xgame.a.world.planet.data.ectype;

import java.util.List;

import cn.xgame.config.o.Stars;

import x.javaplus.collections.Lists;

/**
 * 副本操作中心
 * @author deng		
 * @date 2015-7-13 下午6:16:16
 */
public class EctypeControl {

	// 常规副本
	private List<EctypeLists> general = Lists.newArrayList();
	
	// 非常规副本
	private List<EctypeLists> notGeneral = Lists.newArrayList();
	
	/**
	 * 根据配置表  初始化 副本信息
	 * @param templet
	 */
	public void init( Stars templet ) {
		// 初始常规副本
		String content = templet.ectypes;
		if( !content.isEmpty() ){
			String[] ls = content.split("\\|");
			for( String x : ls ){
				if( x.isEmpty() ) continue;
				EctypeLists o = EctypeLists.generateGeneral( x );
				if( o != null ){
					o.setStarId( templet.id );
					general.add(o);
				}
			}
		}
		// 初始非常规副本
		content = templet.elietectypes;
		if( !content.isEmpty() ){
			String[] ls = content.split("\\|");
			for( String x : ls ){
				if( x.isEmpty() ) continue;
				EctypeLists o = EctypeLists.generateNotGeneral( x );
				if( o != null ){
					o.setStarId( templet.id );
					notGeneral.add(o);
				}
			}
		}
	}
	
	/** 获取常规副本 */
	public List<EctypeLists> getGeneralEctype() { return general; }
	/** 获取非常规副本 */
	public List<EctypeLists> getNotGeneralEctype() { return notGeneral; }

	
	

}
