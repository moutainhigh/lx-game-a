package cn.xgame.a.chat;

import java.util.List;

import cn.xgame.a.chat.o.AxnInfo;
import cn.xgame.a.chat.o.ChatType;

import x.javaplus.collections.Lists;


/**
 * 聊天操作中心
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
	 * 根据频道类型 获取频道列表
	 * @param type
	 * @return
	 */
	public List<AxnInfo> getAxn( ChatType type ) {
		List<AxnInfo> ret = Lists.newArrayList();
		for( AxnInfo axn : axns ){
			if( axn.getType() == type ){
				ret.add(axn);
			}
		}
		return ret;
	}
	
	/**
	 * 频道个数是否已经满
	 * @return
	 */
	public boolean axnIsMax( ChatType type ) {
		int count = 0;
		for( AxnInfo axn : axns ){
			if( axn.getType() == type )
				++count;
		}
		return count >= type.max();
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

	
}
