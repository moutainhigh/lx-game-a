package cn.xgame.a.world.planet.data.ectype;

import java.util.List;


import x.javaplus.collections.Lists;
import x.javaplus.util.Util.Random;


/**
 * 一个副本列表
 * @author deng		
 * @date 2015-7-13 下午6:16:09
 */
public class EctypeLists {

	// 所属星球ID
	private int starId;
	// 副本
	private List<IEctype> ectypes = Lists.newArrayList();
	
	
	/**
	 * 根据配置表  生成一个常规副本列表
	 * @param content
	 * @return
	 */
	public static EctypeLists generateGeneral( String content ) {
		if( content.isEmpty() )
			return null;
		EctypeLists ret = new EctypeLists();
		String[] v 		= content.split(";");
		for( int i = 0; i < v.length; i++ ){
			int nid = Integer.parseInt(v[i]);
			ret.ectypes.add( new GeneralEctype( nid ) );
		}
		return ret;
	}

	/**
	 * 根据配置表  生成一个非常规副本列表
	 * @param content
	 * @return
	 */
	public static EctypeLists generateNotGeneral( String content ) {
		if( content.isEmpty() )
			return null;
		String[] v 	= content.split(";");
		int rand 	= Integer.parseInt(v[1]);
		// 这里随机
		if( Random.get( 0, 10000 ) > rand )
			return null;
		
		EctypeLists ret = new EctypeLists();
		int nid 		= Integer.parseInt(v[0]);
		
		// 这里还要做一下 定时副本的 时间的计算
		// TODO
		NotGeneralEctype o = new NotGeneralEctype(nid);
		ret.ectypes.add( o );
		return ret;
	}

	public int getStarId() {
		return starId;
	}
	public void setStarId(int starId) {
		this.starId = starId;
	}
	public List<IEctype> getEctypes(){
		return ectypes;
	}

}
