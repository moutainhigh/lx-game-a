package cn.xgame.a.player.task;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.Util.Time;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.task.classes.ICondition;
import cn.xgame.a.player.task.classes.ITask;
import cn.xgame.a.player.task.classes.TaskType;
import cn.xgame.a.player.task.classes.condition.WanchengFuben;
import cn.xgame.a.player.task.star.CanTask;
import cn.xgame.a.player.task.star.NpcTask;
import cn.xgame.a.player.task.star.StarTask;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.TaskPo;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.pl.update.Update_1400;
import cn.xgame.utils.LuaUtil;

/**
 * 任务操作中心
 * @author deng		
 * @date 2015-10-24 下午2:12:48
 */
public class TaskControl implements IArrayStream{
	
	private Player root;
	
	// 可接任务
	private List<StarTask> starInTask = Lists.newArrayList();
	
	// 已接任务
	private List<ITask> yetInTask = Lists.newArrayList();
	
	
	public TaskControl(Player player) {
		root = player;
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		int size = buf.readByte();
		for (int i = 0; i < size; i++) {
			StarTask star = new StarTask( buf.readInt() );
			star.wrapBuffer(buf);
			starInTask.add(star);
		}
		size = buf.readShort();
		for (int i = 0; i < size; i++) {
			TaskPo templet = CsvGen.getTaskPo(buf.readInt());
			TaskType type = TaskType.fromNumber( templet.type );
			ITask task = type.create(templet);
			task.wrapBuffer(buf);
			yetInTask.add( task );
		}
	}

	@Override
	public byte[] toBytes() {
		ByteBuf buf = Unpooled.buffer();
		buf.writeByte( starInTask.size() );
		for( StarTask star : starInTask ){
			buf.writeInt( star.getSNID() );
			star.putBuffer(buf);
		}
		buf.writeShort( yetInTask.size() );
		for( ITask task : yetInTask ){
			buf.writeInt( task.getId() );
			task.putBuffer(buf);
		}
		return buf.array();
	}
	
	/**
	 * 刷新一下 已接任务 
	 */
	public void update(){
		List<ITask> remove = Lists.newArrayList();
		for( ITask task : yetInTask ){
			if( task.getEndtime() == 0 )
				continue;
			int curtime = (int) (System.currentTimeMillis()/1000);
			if( curtime >= task.getEndtime() )
				remove.add(task);
		}
		yetInTask.removeAll(remove);
	}
	
	public List<ITask> getYetInTask() {
		return yetInTask;
	}
	
	public StarTask getStarTask( int snid ) {
		for( StarTask star : starInTask ){
			if( star.getSNID() == snid )
				return star;
		}
		return null;
	}
	
	/**
	 * 根据星球&NPC获取任务列表
	 * @param snid
	 * @param npcid
	 * @return
	 */
	public List<CanTask> getCanTask( int snid, int npcid ) {
		StarTask star = getStarTask( snid );
		if( star == null ) return null;
		NpcTask npc = star.getNpc( npcid );
		if( npc == null ) return null;
		return npc.getTasks();
	}
	
	/**
	 * 添加一个任务
	 * @param ret
	 */
	public void addTask( ITask ret ) {
		yetInTask.add(ret);
	}

	/**
	 * 删除一个可接任务
	 * @param snid
	 * @param npcid
	 * @param taskid
	 * @return
	 */
	public boolean removeCanTask( int snid, int npcid, int taskid ) {
		StarTask star = getStarTask( snid );
		if( star == null ) return false;
		NpcTask npc = star.getNpc( npcid );
		if( npc == null ) return false;
		if( npc.remove( taskid ) ){
			if( npc.getTasks().isEmpty() ){
				star.removeNpc( npc.getNpcId() );
				if( star.getNpcs().isEmpty() )
					starInTask.remove(star);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 删除一个已接任务
	 * @param tid
	 */
	public void removeYetTask( int tid ) {
		Iterator<ITask> iter = yetInTask.iterator();
		while(iter.hasNext()){
			if( iter.next().getId() == tid ){
				iter.remove();
				break;
			}
		}
	}
	
	/**
	 * 刷新任务列表
	 */
	public void updateTasks() {
		starInTask.clear();
		List<HomePlanet> homes = WorldManager.o.getAllHomePlanet();
		for( HomePlanet home : homes ){
			String str = LuaUtil.getTaskInfo().getField( "generateTask" ).call( 1, home.getId(), root.getLevel() )[0].getString();
			if( str.isEmpty() ) 
				continue;
			StarTask star = new StarTask( home.getId() );
			String[] content = str.split("\\|");
			for( String x : content ){
				String[] temp = x.split( ":" );
				if( temp.length < 2 )
					continue;
				String[] ls = temp[1].split( ";" );
				NpcTask npc = new NpcTask( Integer.parseInt( temp[0] ) );
				for( String o : ls ){
					int id = Integer.parseInt( o );
					if( CsvGen.getTaskPo(id) == null ) 
						continue;
					CanTask ct = new CanTask( id );
					ct.setEndtime( (int) (Time.refTimeInMillis(24, 0, 0)/1000) );
					npc.addTask( ct );
				}
				star.addNpc( npc );
			}
			starInTask.add(star);
		}
	}

	/**
	 * 执行 完成副本
	 * @param chapterId
	 */
	public void execute( ConType type, Object ... objects ) {
		List<ITask> remove = Lists.newArrayList();
		for( ITask task : yetInTask ){
			List<ICondition> conditions = task.getConditions();
			for( ICondition con : conditions ){
				if( con.isComplete() || con.getType() != type )
					continue;
				execute( type, con, objects );
			}
			// 这里如果任务完成了 那么就要发放奖励
			if( task.isComplete() ){
				List<IProp> ret = task.executeAward( root );
				// 通知前端
				((Update_1400)Events.UPDATE_1400.toInstance()).run( task.getId(), root, ret );
				// 然后删除掉
				remove.add(task);
			}
		}
		yetInTask.removeAll(remove);
	}
	private void execute( ConType type, ICondition con, Object[] objects ) {
		switch ( type ) {
		case DAODADIDIAN:
			break;
		case WANCHENGFUBEN:
			((WanchengFuben) con).execute( (Integer) objects[0] );
			break;
		case XIAOHAODAOJU:
			break;
		}
	}

}
