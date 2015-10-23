package cn.xgame.a.chat.axn.info;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.chat.axn.classes.ChatType;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.player.u.Player;

/**
 * 一个频道数据
 * @author deng		
 * @date 2015-8-2 下午12:53:08
 */
public class AxnInfo{

	// 频道唯一ID
	private int  		axnId;
	
	// 频道类型
	private ChatType 	type;
	
	// 频道名字
	private String		name;
	
	// 频道对应玩家列表
	private List<IAxnCrew> axnCrews = Lists.newArrayList();

	public AxnInfo(ChatType type, int axnId) {
		this.type 	= type;
		this.axnId 	= axnId;
	}
	
	public int getAxnId() { return axnId; }
	public ChatType getType() { return type; }
	public List<IAxnCrew> getAxnCrews() { return axnCrews; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	/**
	 * 人数是否已经满了
	 * @return
	 */
	public boolean isMaxmember() {
		return axnCrews.size() >= type.maxmember();
	}
	
	/**
	 * 玩家是否在该频道
	 * @param player
	 * @return
	 */
	public boolean isHave( Player player ) {
		return getAxnCrew( player.getUID() ) != null;
	}
	public boolean isHave( String uid ){
		return getAxnCrew( uid ) != null;
	}
	
	/**
	 * 根据玩家UID 获取玩家信息
	 * @param uid
	 * @return
	 */
	public IAxnCrew getAxnCrew( String uid ){
		for( IAxnCrew crew : axnCrews ){
			if( crew.getUid().equals(uid) )
				return crew;
		}
		return null;
	}
	
	/**
	 * 删除一个组员
	 * @param uid
	 */
	public void removeCrew( String uid ) {
		Iterator<IAxnCrew> iter = axnCrews.iterator();
		while( iter.hasNext() ){
			if( iter.next().getUid().equals( uid ) ){
				iter.remove();
				return ;
			}
		}
	}
	
	/**
	 * 添加群聊频道 组员
	 * @param player
	 */
	public void appendGroupCrew( Player player ) {
		if( getAxnCrew( player.getUID() ) != null )
			return ;
		GroupAxnCrew crew = new GroupAxnCrew();
		crew.setUid( player.getUID() );
		crew.setName( player.getNickname() );
		crew.setHeadIco( player.getHeadIco() );
		crew.setSocket( player.getCtx() );
		axnCrews.add(crew);
	}

	/**
	 * 添加组队频道 组员
	 * @param player
	 * @param fid
	 */
	public void appendTeamCrew(Player player, byte fid) {
		if( getAxnCrew( player.getUID() ) != null )
			return ;
		TeamAxnCrew crew = new TeamAxnCrew();
		crew.setUid( player.getUID() );
		crew.setName( player.getNickname() );
		crew.setHeadIco( player.getHeadIco() );
		crew.setSocket( player.getCtx() );
		crew.setFid(fid);
		axnCrews.add(crew);
	}

	/**
	 * 私聊 - 获取另外一个人的名字
	 * @param uid
	 * @return
	 */
	public String getPrivateName( String uid ) {
		for( IAxnCrew crew : axnCrews ){
			if( crew.getUid().equals(uid) )
				continue;
			return crew.getName();
		}
		return "";
	}

}
