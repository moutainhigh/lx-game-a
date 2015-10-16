package cn.xgame.a.player.manor.classes;

/**
 * 产出
 * @author deng		
 * @date 2015-10-15 下午5:37:56
 */
public class Goods {
	
	private int id;
	
	private float count;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getCount() {
		return count;
	}
	public void addCount(float count) {
		this.count += count;
	}
	public void clear() {
		this.count = 0;
	}
	
}
