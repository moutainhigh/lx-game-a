package cn.xgame.a.world.planet.data.building;

import cn.xgame.a.world.planet.data.vote.Vote;

/**
 * 未建筑 的建筑 - 先投票 然后经过时间 建筑<br>
 * 建筑中和投票的 都用这个对象
 * @author deng		
 * @date 2015-6-30 下午4:59:04
 */
public class UnBuildings extends Buildings{

	// 记录时间 - 用于建筑中的
	private int rTime;
	
	// 投票器 - 用于投票的
	private Vote vote;
	
	public UnBuildings( int id ) {
		super(id);
	}

	public int getrTime() {
		return rTime;
	}
	public void setrTime(int rTime) {
		this.rTime = rTime;
	}
	public Vote getVote() {
		return vote;
	}
	public void setVote(Vote vote) {
		this.vote = vote;
	}
	
	
}
