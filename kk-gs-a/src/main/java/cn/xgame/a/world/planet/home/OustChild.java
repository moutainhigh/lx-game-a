package cn.xgame.a.world.planet.home;

import cn.xgame.a.player.u.Player;

/**
 * 驱逐元老 信息
 * @author deng		
 * @date 2015-7-10 下午5:27:42
 */
public class OustChild {

	// 玩家唯一ID
	private String uid;
	
	// 投票说明
	private String explain;
	
	
	// 发起投票人
	private String sponsorUid;
	private String sponsorName;
	
	public OustChild(Player player, String uid, String explain) {
		sponsorUid 		= player.getUID();
		sponsorName 	= player.getNickname();
		this.uid 		= uid;
		this.explain 	= explain;
	}

	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public String getSponsorUid() {
		return sponsorUid;
	}
	public void setSponsorUid(String sponsorUid) {
		this.sponsorUid = sponsorUid;
	}
	public String getSponsorName() {
		return sponsorName;
	}
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

}
