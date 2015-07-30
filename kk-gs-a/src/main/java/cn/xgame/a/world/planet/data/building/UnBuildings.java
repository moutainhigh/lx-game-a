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

	// 记录时间 - 用于建筑中的
	private int rTime;
	
	// 投票器 - 用于投票的
	private Vote vote = null;
	
	public UnBuildings( int id ) {
		super(id);
	}

	public UnBuildings(SbuildingPo templet, byte index) {
		super( templet, index );
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
		if( rTime == -1 )
			return -1;
		if( templet().needtime == 0 ) 
			return 0;
		return (int) (System.currentTimeMillis()/1000 - rTime);
	}

	/**
	 * 是否建筑完成
	 * @return
	 */
	public boolean isComplete() {
		return getPastTime() >= templet().needtime || templet().needtime == 0;
	}
	
	
}
