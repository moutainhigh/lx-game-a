package cn.xgame.a.player.task.info;

/**
 * 已完成任务
 * @author deng		
 * @date 2015-12-1 下午3:27:21
 */
public class CompleteTask {
	
	// 任务id
	private int id;
	
	// 完成时间
	private int completeTime;
	
	public CompleteTask( int id ) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	public int getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(int completeTime) {
		this.completeTime = completeTime;
	}
	
}
