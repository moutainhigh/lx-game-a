package cn.xgame.a.world.planet.data.building;

import cn.xgame.a.world.planet.data.vote.Vote;
import cn.xgame.config.o.SbuildingPo;

/**
 * 未建筑 的建筑 - 先投票 然后经过时间 建筑<br>
 * 建筑中和投票的 都用这个对象
 * @author deng		
 * @date 2015-6-30 下午4:59:04
 */
public class UnBuildings extends Buildings{

	// 结束时间 - 用于建筑中的
	private int endtime;
	
	// 投票器 - 用于投票的
	private Vote vote = null;
	
	public UnBuildings( int id ) {
		super(id);
	}

	public UnBuildings(SbuildingPo templet, byte index) {
		super( templet, index );
	}

	public int getEndtime() {
		return endtime;
	}
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	public Vote getVote() {
		return vote;
	}
	public void setVote(Vote vote) {
		this.vote = vote;
	}

	/**
	 * 是否建筑完成
	 * @return
	 */
	public boolean isComplete() {
		return endtime != -1 && (int)(System.currentTimeMillis()/1000) >= endtime;
	}

	
}
