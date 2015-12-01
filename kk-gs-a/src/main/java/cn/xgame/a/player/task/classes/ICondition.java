package cn.xgame.a.player.task.classes;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.o.TaskcndPo;


/**
 * 任务条件
 * @author deng		
 * @date 2015-10-25 上午1:33:51
 */
public abstract class ICondition implements IBufferStream{
	
	// 条件ID
	private final TaskcndPo templet;
	
	// 条件类型
	private final ConType type;
	
	// 是否完成
	protected boolean isComplete = false;
	
	public ICondition( ConType type, TaskcndPo templet ) {
		this.templet = templet;
		this.type = type;
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeByte( isComplete ? 1 : 0 );
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		isComplete = buf.readByte() == 1;
	}
	
	/**
	 * 接任务执行
	 */
	public abstract void beginExecute(Player player);
	
	/**
	 * 执行任务条件
	 * @param objects
	 */
	public abstract void execute(Object[] objects);
	
	/**
	 * 结束执行
	 */
	public abstract void endExecute(Player player);
	
	/**
	 * 是否完成
	 */
	public boolean isComplete( Player player ){ return isComplete; }
	
	public void isComplete( boolean isComplete ){
		this.isComplete = isComplete;
	}

	public TaskcndPo templet() { 
		return templet; 
	}

	public ConType getType() {
		return type;
	}

}
