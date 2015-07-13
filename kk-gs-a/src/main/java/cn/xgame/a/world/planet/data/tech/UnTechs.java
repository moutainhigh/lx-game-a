package cn.xgame.a.world.planet.data.tech;

import cn.xgame.a.world.planet.data.vote.Vote;

/**
 * 还没学习的科技<br>
 * 投票中和研究中的
 * @author deng		
 * @date 2015-6-30 下午5:55:17
 */
public class UnTechs extends Techs{

	// 记录时间 - 用于建筑中的
	private int rTime;
	
	// 投票器 - 用于投票的
	private Vote vote;
	
	public UnTechs(int id) {
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

	/**
	 * 获取已经过去的时间 - 单位秒
	 * @return
	 */
	public int getPastTime() {
		return 10;
	}

	/**
	 * 是否研究完成
	 * @return
	 */
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}
}
