package cn.xgame.a.player.ship.o.v;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.player.u.Player;

/**
 * 临时记录信息
 * @author deng		
 * @date 2015-8-2 下午8:32:56
 */
public class TempRecordInfo {
	
	
	private List<TempInviteData> tids = Lists.newArrayList();
	
	/**
	 * 记录玩家邀请组队信息
	 * @param suid 
	 * @param to
	 */
	public void recordInviteTeam( Player to) {
		TempInviteData data = new TempInviteData();
		data.uname 	= to.getUID();
		tids.add(data);
	}

	/**
	 * 是否有邀请该玩家组队信息
	 * @param player
	 * @return
	 */
	public boolean isHaveInviteTeam(Player player) {
		for( TempInviteData data : tids ){
			if( data.uname.equals(player.getUID()) )
				return true;
		}
		return false;
	}

	/**
	 * 删除一个邀请组队记录
	 * @param player
	 */
	public void removeInviteTeam( Player player ) {
		Iterator<TempInviteData> iter = tids.iterator();
		while( iter.hasNext() ){
			TempInviteData o = iter.next();
			if( o.uname.equals( player.getUID() ) ){
				iter.remove();
				return;
			}
		}
	}

}

class TempInviteData{
	public String uname; // 我邀请的人
}

