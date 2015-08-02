package cn.xgame.a.chat.o;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.player.u.Player;
import cn.xgame.utils.Logs;

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
	 * 创建一个频道 必须有两个玩家才能创建
	 * @param tempaxn
	 * @param axnId
	 * @param p1
	 * @param p2
	 */
	public void createAxn( ChatType type, int axnId, Player p1, Player p2 ) {
		if( getAXNInfo(axnId) != null ){
			Logs.error( "创建频道出错 已经有这个频道了 axnId=" + axnId );
			return ;
		}
		AxnInfo axn = new AxnInfo( type, axnId );
		axn.appendCrew( p1 );
		axn.appendCrew( p2 );
		axns.add(axn);
	}

	/**
	 * 将玩家添加到频道
	 * @param axnId
	 * @param p
	 */
	public void appendAxn( int axnId, Player p ) {
		AxnInfo axn = getAXNInfo(axnId);
		axn.appendCrew(p);
	}
	
}
