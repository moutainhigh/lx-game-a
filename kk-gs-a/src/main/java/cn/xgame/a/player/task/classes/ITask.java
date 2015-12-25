package cn.xgame.a.player.task.classes;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.mail.classes.MailType;
import cn.xgame.a.player.mail.info.MailInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.TaskPo;
import cn.xgame.config.o.TaskcndPo;

/**
 * 任务基类
 * @author deng		
 * @date 2015-10-24 下午3:01:02
 */
public class ITask implements IBufferStream {
	
	private final TaskPo templet ;
	
	// 任务类型
	private final TaskType type;
	
	// 奖励列表 
	private List<AwardInfo> awards = Lists.newArrayList();
	
	// 条件
	private List<ICondition> conditions = Lists.newArrayList();
	
	// 限时 剩余时间
	private int endtime = 0;
	
	public ITask( TaskType type, TaskPo templet ){
		this.templet = templet;
		this.type = type;
		initCondition( );
		initAwards();
		if( templet.time != 0 )
			endtime = (int) (System.currentTimeMillis()/1000 + templet.time);
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
	private void initCondition() {
		String[] str = templet.course.split(";");
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
	public int getEndtime() {
		return endtime;
	}
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeByte( conditions.size() );
		for( ICondition con : conditions ){
			buf.writeInt( con.templet().id );
			con.putBuffer(buf);
		}
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		byte size = buf.readByte();
		for (int i = 0; i < size; i++) {
			int id = buf.readInt();
			ICondition con = getCondition(id);
			if( con != null ){
				con.wrapBuffer(buf);
			}else{
				break;
			}
		}
	}
	private ICondition getCondition(int id) {
		for( ICondition con : conditions ){
			if( con.templet().id == id ){
				return con;
			}
		}
		return null;
	}
	
	public List<ICondition> getConditions(){
		return conditions;
	}
	
	/**
	 * 任务是否完成
	 * @return
	 */
	public boolean isComplete(Player root) {
		for( ICondition con : conditions ){
			if( !con.isComplete(root) )
				return false;
		}
		return true;
	}
	
	/**
	 * 发放奖励 直接发邮件
	 * @param root
	 * @return 
	 */
	public void executeAward( Player root ) {
		// 直接奖励金币
		root.changeCurrency( templet.money, "任务奖励获得" );
		// 直接发送经验
		root.addExp( templet.exp );
		// 道具 用邮件的形式发送 如果有的话
		if( !awards.isEmpty() ){
			MailInfo mail = new MailInfo( MailType.SYSTEM, templet.mailtitle, templet.mailwrite );
			for( AwardInfo award : awards )
				mail.addProp( award.getId(), award.getCount() );
			root.getMails().addMail(mail);
		}
	}
	
	public void executeEnd(Player player) {
		for( ICondition con : conditions )
			con.endExecute(player);
	}
	public void executeBegin(Player player) {
		for( ICondition con : conditions )
			con.beginExecute(player);
	}



}
