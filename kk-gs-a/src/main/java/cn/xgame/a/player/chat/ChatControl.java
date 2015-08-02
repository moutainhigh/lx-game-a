package cn.xgame.a.player.chat;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.chat.o.ChatType;
import cn.xgame.a.player.u.Player;

/**
 * 玩家聊天信息 操作中心
 * @author deng		
 * @date 2015-8-2 下午4:01:50
 */
public class ChatControl {

	// 临时频道
	private List<Integer> tempaxn = Lists.newArrayList();
	
	
	public ChatControl(Player player) {
	}

	/**
	 * 根据频道类型获取 频道ID列表
	 * @param type
	 * @return
	 */
	public List<Integer> getAxn( ChatType type ){
		if( type == ChatType.TEMPAXN )
			return tempaxn;
		return null;
	}
	
	
	/**
	 * 获取频道个数是否满
	 * @param type
	 * @return
	 */
	public boolean axnIsMax(ChatType type) {
		List<Integer> axns = getAxn( type );
		return axns == null ? true : axns.size() >= type.max();
	}

	/**
	 * 添加一个频道
	 * @param type
	 * @param axnId
	 */
	public void appendAxn(ChatType type, int axnId) {
		List<Integer> axns = getAxn( type );
		if( axns.indexOf(axnId) == -1 )
			axns.add( axnId );
	}

}
