package cn.xgame.a.chat.axn.info;

import cn.xgame.a.chat.axn.classes.IAxnCrew;

/**
 * 组队频道 玩家信息
 * @author deng		
 * @date 2015-8-2 下午10:05:32
 */
public class TeamAxnCrew extends IAxnCrew{

	// 舰队ID
	private byte fid = 0;
	
	public byte getFid() {
		return fid;
	}
	public void setFid(byte fid) {
		this.fid = fid;
	}
	
}
