package cn.xgame.a.player.task;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.task.classes.ICondition;
import cn.xgame.a.player.task.classes.ITask;
import cn.xgame.a.player.task.classes.TaskType;
import cn.xgame.a.player.task.info.CanTask;
import cn.xgame.a.player.task.info.CompleteTask;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.TaskPo;

/**
 * 任务操作中心
 * @author deng		
 * @date 2015-10-24 下午2:12:48
 */
public class TaskControl implements IArrayStream,ITransformStream{
	
	private Player root;
	
	// 可接任务
	private List<CanTask> canTasks = Lists.newArrayList();
	
	// 已接任务
	private List<ITask> yetInTasks = Lists.newArrayList();
	
	// 已完成任务
	private List<CompleteTask> completeTasks = Lists.newArrayList();
	
	
	public TaskControl(Player player) {
		root = player;
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		// 可接任务
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			CanTask task = new CanTask( buf.readInt() );
			task.setLooptimes( buf.readByte() );
			canTasks.add(task);
		}
		// 已接任务
		size = buf.readByte();
		for (int i = 0; i < size; i++) {
			TaskPo templet = CsvGen.getTaskPo(buf.readInt());
			TaskType type = TaskType.fromNumber( templet.tasktype );
			ITask task = type.create(templet);
			task.wrapBuffer(buf);
			yetInTasks.add( task );
		}
		// 已完成任务
		size = buf.readInt();
		for (int i = 0; i < size; i++) {
			CompleteTask task = new CompleteTask( buf.readInt() );
			task.setCompleteTime( buf.readInt() );
			completeTasks.add(task);
		}
		update();
	}

	@Override
	public byte[] toBytes() {
		update();
		ByteBuf buf = Unpooled.buffer();
		// 可接任务
		buf.writeInt( canTasks.size() );
		for( CanTask task : canTasks ){
			buf.writeInt( task.getId() );
			buf.writeByte( task.getLooptimes() );
		}
		// 已接任务
		buf.writeByte( yetInTasks.size() );
		for( ITask task : yetInTasks ){
			buf.writeInt( task.getId() );
			task.putBuffer(buf);
		}
		// 已完成任务
		buf.writeInt( completeTasks.size() );
		for ( CompleteTask task : completeTasks ) {
			buf.writeInt( task.getId() );
			buf.writeInt( task.getCompleteTime() );
		}
		return buf.array();
	}
	
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt(canTasks.size());
		for( CanTask task : canTasks ){
			buffer.writeInt(task.getId());
			buffer.writeByte(task.getLooptimes());
		}
		buffer.writeByte(yetInTasks.size());
		for( ITask task : yetInTasks ){
			buffer.writeInt( task.getId() );
			buffer.writeInt( task.getEndtime() );
			List<ICondition> conditions = task.getConditions();
			buffer.writeByte( conditions.size() );
			for( ICondition con : conditions ){
				buffer.writeInt( con.templet().id );
				buffer.writeByte( con.isComplete(root) ? 1 : 0 );
			}
		}
		buffer.writeInt( completeTasks.size() );
		for( CompleteTask task : completeTasks ){
			buffer.writeInt(task.getId());
		}
	}
	
	/**
	 * 刷新限时任务 
	 */
	private void update(){
		Iterator<ITask> iter = yetInTasks.iterator();
		while(iter.hasNext()){
			ITask next = iter.next();
			if( next.getEndtime() == 0 )
				continue;
			int t = (int) (System.currentTimeMillis()/1000);
			if( t >= next.getEndtime() )
				iter.remove();
		}
	}
	
	/**
	 * 刷新每日任务次数
	 */
	public void updateEverydayTimes() {
		Iterator<CanTask> iter = canTasks.iterator();
		while(iter.hasNext()){
			CanTask task = iter.next();
			task.setLooptimes(0);
		}
	}
	
	public List<CanTask> getCanTasks(){
		return canTasks;
	}
	public void addCanTask(List<Integer> ret) {
		if( ret == null || ret.isEmpty() )
			return;
		for( int id : ret ){
			CanTask task = new CanTask(id);
			canTasks.add(task);
		}
	}
	public CanTask getCanTask(int taskid) {
		for( CanTask task : canTasks ){
			if( task.getId() == taskid )
				return task;
		}
		return null;
	}
	/**
	 * 删除一个可接任务
	 * @param taskid
	 * @return
	 */
	public boolean removeCanTask( int taskid ) {
		Iterator<CanTask> iter = canTasks.iterator();
		while(iter.hasNext()){
			CanTask next = iter.next();
			if( next.getId() == taskid ){
				iter.remove();
				return true;
			}
		}
		return false;
	}
	
	
	public List<ITask> getYetInTasks() {
		return yetInTasks;
	}
	public ITask getYetInTask( int taskid ){
		for( ITask task : yetInTasks ){
			if( task.getId() == taskid )
				return task;
		}
		return null;
	}
	/**
	 * 添加一个任务
	 * @param ret
	 */
	public void addTask( ITask ret ) {
		if( yetInTasks.size() >= 100 )
			return;
		ret.executeBegin(root);
		yetInTasks.add(ret);
	}
	/**
	 * 删除一个已接任务
	 * @param taskid
	 */
	public void removeYetTask( int taskid ) {
		Iterator<ITask> iter = yetInTasks.iterator();
		while(iter.hasNext()){
			ITask next = iter.next();
			if( next.getId() == taskid ){
				iter.remove();
				break;
			}
		}
	}
	
	/**
	 * 添加一个已经完成的任务
	 * @param taskid
	 */
	public void addCompletTask(int taskid) {
		CompleteTask e = new CompleteTask(taskid);
		e.setCompleteTime((int) (System.currentTimeMillis()/1000));
		completeTasks.add(e);
	}

	/**
	 * 执行任务
	 * @param type 条件类型
	 * @param objects 对应参数
	 */
	public void execute( ConType type, Object ... objects ) {
		Iterator<ITask> iter = yetInTasks.iterator();
		while( iter.hasNext() ){
			ITask task = iter.next();
			List<ICondition> conditions = task.getConditions();
			for( ICondition con : conditions ){
				if( con.isComplete(root) || con.getType() != type )
					continue;
				con.execute(objects);
			}
		}
	}


	

}
