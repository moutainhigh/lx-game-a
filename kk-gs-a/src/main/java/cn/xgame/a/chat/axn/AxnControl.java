package cn.xgame.a.chat.axn;

import java.util.Iterator;
import java.util.List;

import cn.xgame.a.chat.axn.classes.ChatType;
import cn.xgame.a.chat.axn.info.AxnInfo;

import x.javaplus.collections.Lists;


/**
 * 频道操作中心
 * @author deng		
 * @date 2015-8-2 下午12:49:07
 */
public class AxnControl {

	//频道列表
	private List<AxnInfo> axns = Lists.newArrayList();
	

	/**
	 * 根据唯一ID获取对应频道信息
	 * @param id
	 * @return
	 */
	public AxnInfo getAXNInfo( int id ) {
		for( AxnInfo axn : axns ){
			if( axn.getAxnId() == id )
				return axn;
		}
		return null;
	}
	
	/**
	 * 创建一个频道
	 * @param tempaxn
	 * @param p1
	 * @param p2
	 * @return 
	 * @throws Exception 
	 */
	public AxnInfo createAxn( ChatType type ) throws Exception {
		AxnInfo axn = new AxnInfo( type, type.generateUID() );
		axns.add(axn);
		return axn;
	}
	
	/**
	 * 删除一个 频道
	 * @param id
	 */
	public void removeAxn( int id ) {
		Iterator<AxnInfo> iter = axns.iterator();
		while( iter.hasNext() ){
			AxnInfo next = iter.next();
			if( next.getAxnId() == id ){
				next.getType().appendUID( id );
				iter.remove();
				return;
			}
		}
	}

}
