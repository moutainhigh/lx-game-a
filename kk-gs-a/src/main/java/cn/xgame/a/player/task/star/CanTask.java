package cn.xgame.a.player.task.star;

/**
 * 可接任务
 * @author deng		
 * @date 2015-10-24 下午2:56:51
 */
public class CanTask {
	
	// 任务id
	private int id;
	
	// 时效 结束时间
	private int endtime;
	
	
	public CanTask( int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
}
