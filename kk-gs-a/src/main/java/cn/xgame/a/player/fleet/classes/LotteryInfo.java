package cn.xgame.a.player.fleet.classes;

/**
 * 一个抽奖信息
 * @author deng		
 * @date 2015-11-3 下午6:59:33
 */
public class LotteryInfo {

	// 章节ID
	private int chapterId;
	
	// 奖品类型  1.银奖 2.金奖
	private int type;
	
	// 抽奖个数 
	private int count;
	
	public LotteryInfo( int chapterId, int type, int count ) {
		this.chapterId = chapterId;
		this.type = type;
		this.count = count;
	}
	
	public int getChapterId() {
		return chapterId;
	}
	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
