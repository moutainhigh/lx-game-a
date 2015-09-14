package cn.xgame.a.world.planet.data.tech;

import cn.xgame.a.world.planet.data.vote.Vote;
import cn.xgame.config.o.TechPo;

/**
 * 还没学习的科技<br>
 * 投票中和研究中的
 * @author deng		
 * @date 2015-6-30 下午5:55:17
 */
public class UnTechs extends Techs{

	// 结束时间 - 用于建筑中的
	private int endtime;
	
	// 投票器 - 用于投票的
	private Vote vote = null;
	
	public UnTechs(int id) {
		super(id);
	}
	
	public UnTechs(TechPo templet) {
		super(templet);
	}

	public Vote getVote() {
		return vote;
	}
	public void setVote(Vote vote) {
		this.vote = vote;
	}
	public int getEndtime() {
		return endtime;
	}
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	/**
	 * 是否研究完成
	 * @return
	 */
	public boolean isComplete() {
		return endtime != -1 && (int)(System.currentTimeMillis()/1000) >= endtime;
	}

}
