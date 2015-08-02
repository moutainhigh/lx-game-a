package cn.xgame.a.chat.o;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.player.u.Player;

/**
 * 一个频道数据
 * @author deng		
 * @date 2015-8-2 下午12:53:08
 */
public class AxnInfo {

	// 频道唯一ID
	private int  		axnId;
	
	// 频道类型
	private ChatType 	type;
	
	// 频道对应玩家列表
	private List<AxnCrew> axnCrews = Lists.newArrayList();

	public AxnInfo(ChatType type, int axnId) {
		this.type 	= type;
		this.axnId 	= axnId;
	}
	
	public int getAxnId() { return axnId; }
	public ChatType getType() { return type; }
	public List<AxnCrew> getAxnCrews() { return axnCrews; }

	/**
	 * 人数是否已经满了
	 * @return
	 */
	public boolean isMaxmember() {
		return axnCrews.size() >= type.maxmember();
	}
	
	/**
	 * 玩家是否有权限
	 * @param player
	 * @return
	 */
	public boolean isHavePrivilege( Player player ) {
		return getAxnCrew( player.getUID() ) != null;
	}
	
	/**
	 * 根据玩家UID 获取玩家信息
	 * @param uid
	 * @return
	 */
	public AxnCrew getAxnCrew( String uid ){
		for( AxnCrew crew : axnCrews ){
			if( crew.getUid().equals(uid) )
				return crew;
		}
		return null;
	}
	
	/**
	 * 添加组员
	 * @param player
	 */
	public void appendCrew(Player player) {
		if( getAxnCrew( player.getUID() ) != null )
			return ;
		AxnCrew crew = new AxnCrew();
		crew.setUid( player.getUID() );
		crew.setName( player.getNickname() );
		crew.setHeadIco( player.getHeadIco() );
		crew.setSocket( player.getCtx() );
		axnCrews.add(crew);
	}



	
}
