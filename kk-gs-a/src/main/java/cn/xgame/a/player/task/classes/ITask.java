package cn.xgame.a.player.task.classes;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.TaskPo;
import cn.xgame.config.o.TaskcndPo;

/**
 * 任务基类
 * @author deng		
 * @date 2015-10-24 下午3:01:02
 */
public class ITask implements ITransformStream,IBufferStream {
	
	private final TaskPo templet ;
	
	// 任务类型
	private final TaskType type;
	
	// 奖励列表 
	private List<AwardInfo> awards = Lists.newArrayList();
	
	// 限时 结束时间
	private int endtime = 0;
	
	// 条件
	private List<ICondition> conditions = Lists.newArrayList();
	
	public ITask( TaskType type, TaskPo templet ){
		this.templet = templet;
		this.type = type;
		initCondition( );
		initEndtime();
		initAwards();
	}
	private void initAwards() {
		if( templet.award.isEmpty() )
			return;
		String[] str = templet.award.split("\\|");
		for( String x : str ){
			String[] o = x.split( ";" );
			AwardInfo a = new AwardInfo( Integer.parseInt( o[0] ), Integer.parseInt( o[1] ) );
			awards.add(a);
		}
	}
	private void initEndtime() {
		if( templet.timelimit == 0 )
			return ;
		endtime = (int) (System.currentTimeMillis()/1000 + templet.timelimit); 
	}
	private void initCondition() {
		String[] str = templet.completecnd.split(";");
		for( String x : str ){
			TaskcndPo templet = CsvGen.getTaskcndPo( Integer.parseInt( x ) );
			if( templet == null )
				continue;
			ConType type = ConType.fromNumber( templet.type );
			conditions.add( type.create( templet ) );
		}
	}
	
	public TaskPo templet(){ return templet; }
	public int getId() { return templet.id; }
	public TaskType type(){ return type; }
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeByte( conditions.size() );
		for( ICondition con : conditions ){
			buf.writeInt( con.getId() );
			buf.writeInt( con.getValue() );
		}
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		byte size = buf.readByte();
		for (int i = 0; i < size; i++) {
			int id = buf.readInt();
			int value = buf.readInt();
			for( ICondition con : conditions ){
				if( con.getId() == id ){
					con.setValue(value);
					break;
				}
			}
		}
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( templet.id );
		buffer.writeInt( endtime );
		buffer.writeByte( conditions.size() );
		for( ICondition tc : conditions ){
			buffer.writeInt( tc.getId() );
			buffer.writeInt( tc.isComplete() ? -1 : tc.getValue() );
		}
	}

	public int getEndtime() {
		return endtime;
	}
	
	public List<ICondition> getConditions(){
		return conditions;
	}
	
	/**
	 * 任务是否完成
	 * @return
	 */
	public boolean isComplete() {
		for( ICondition con : conditions ){
			if( !con.isComplete() )
				return false;
		}
		return true;
	}
	
	/**
	 * 发放奖励
	 * @param root
	 * @return 
	 */
	public List<IProp> executeAward( Player root ) {
		List<IProp> ret = Lists.newArrayList();
		
		// 添加道具
		StarDepot depots = root.getDepots();
		for( AwardInfo award : awards ){
			ret.addAll( depots.appendProp( award.getId(), award.getCount() ) );
		}
		// 添加经验
		root.addExp( templet.exp );
		
		return ret;
	}

}
