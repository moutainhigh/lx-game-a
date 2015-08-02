package cn.xgame.a.chat;

import java.util.List;

import cn.xgame.a.chat.o.AxnInfo;
import cn.xgame.a.chat.o.ChatType;
import cn.xgame.a.player.u.Player;

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
	 * 玩家离线 处理
	 * @param player
	 */
	public void playerOffline( Player player ){
		List<Integer> ls = player.getChatAxns().getAllAxn();
		for( int axnId : ls ){
			AxnInfo axn = getAXNInfo( axnId );
			if( axn == null ) {
				player.getChatAxns().removeAxn( axnId );
				continue;
			}
			axn.offline( player );
		}
	}
	
	
}
