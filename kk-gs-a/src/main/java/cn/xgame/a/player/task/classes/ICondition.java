package cn.xgame.a.player.task.classes;


/**
 * 任务条件
 * @author deng		
 * @date 2015-10-25 上午1:33:51
 */
public abstract class ICondition {
	
	// 条件ID
	private final int id;
	
	// 条件类型
	private final ConType type;
	
	// 对应数值
	private int value = 0;
	
	public ICondition( ConType type, int id ) {
		this.id = id;
		this.type = type;
	}

	/**
	 * 是否完成
	 */
	public abstract boolean isComplete();

	public int getId() { 
		return id; 
	}

	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void addValue( int value ) {
		this.value += value;
	}

	public ConType getType() {
		return type;
	}
	
}
