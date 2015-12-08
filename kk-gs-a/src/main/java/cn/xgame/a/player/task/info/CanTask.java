package cn.xgame.a.player.task.info;

/**
 * 可接任务
 * @author deng		
 * @date 2015-10-24 下午2:56:51
 */
public class CanTask {
	
	// 任务id
	private int id;
	
	// 循环次数
	private int looptimes = 0;
	
	public CanTask( int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public int getLooptimes() {
		return looptimes;
	}

	public void setLooptimes(int looptimes) {
		this.looptimes = looptimes;
	}

	public void addLooptimes(int value) {
		this.looptimes += value;
		if( this.looptimes < 0 )
			this.looptimes = 0;
	}
}
