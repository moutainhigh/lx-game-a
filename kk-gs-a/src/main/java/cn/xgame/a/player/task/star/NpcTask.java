package cn.xgame.a.player.task.star;

import io.netty.buffer.ByteBuf;

import java.util.Iterator;
import java.util.List;

import cn.xgame.a.IBufferStream;


import x.javaplus.collections.Lists;
import x.javaplus.util.Util.Time;


/**
 * NPC任务列表
 * @author deng		
 * @date 2015-10-24 下午2:51:41
 */
public class NpcTask implements IBufferStream{
	
	// NPC
	private int npcId ;
	
	// 可接任务列表
	private List<CanTask> tasks = Lists.newArrayList();
	
	
	public NpcTask( int npcid ) {
		this.npcId = npcid;
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeByte( tasks.size() );
		for( CanTask can : tasks ){
			buf.writeInt( can.getId() );
		}
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			CanTask ct = new CanTask( buf.readInt() );
			ct.setEndtime( (int) (Time.refTimeInMillis(24, 0, 0)/1000) );
			tasks.add(ct);
		}
	}
	
	public int getNpcId() {
		return npcId;
	}
	public List<CanTask> getTasks(){
		return tasks;
	}
	
	/**
	 * 添加一个任务
	 * @param ct
	 */
	public void addTask( CanTask ct ) {
		tasks.add( ct );
	}
	
	/**
	 * 删除一个任务
	 * @param taskid
	 * @return
	 */
	public boolean remove( int taskid ) {
		Iterator<CanTask> iter = tasks.iterator();
		while( iter.hasNext() ){
			if( iter.next().getId() == taskid ){
				iter.remove();
				return true;
			}
		}
		return false;
	}
	
}
