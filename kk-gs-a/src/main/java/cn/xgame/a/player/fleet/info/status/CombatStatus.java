package cn.xgame.a.player.fleet.info.status;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.fleet.classes.IStatus;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;

/**
 * 战斗状态
 * @author deng		
 * @date 2015-9-11 上午12:39:04
 */
public class CombatStatus extends IStatus{
	
	// 副本类型 1.常规副本 2.普通限时 3.特殊限时
	private byte type;
	
	// 章节ID
	private int chapterId;
	
	// 副本ID
	private int ectypeId;
	
	// 战斗结束时间
	private int endtime;
	private int ctime;
	
	// 是否胜利
	private byte isWin;
	
	// 奖励列表
	private List<AwardInfo> awards = Lists.newArrayList();
	
	// 评分
	private int score;
	
	public CombatStatus() {
		super( StatusType.COMBAT );
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		super.putBuffer(buf);
		buf.writeByte( type );
		buf.writeInt( chapterId );
		buf.writeInt( ectypeId );
		buf.writeInt( endtime );
		buf.writeInt( ctime );
		buf.writeByte( isWin );
		buf.writeByte( awards.size() );
		for( AwardInfo award : awards )
			award.buildTransformStream(buf);
		buf.writeInt( score );
	}
	
	@Override
	public void wrapBuffer(ByteBuf buf) {
		super.wrapBuffer(buf);
		this.type 		= buf.readByte();
		this.chapterId 	= buf.readInt();
		this.ectypeId 	= buf.readInt();
		this.endtime 	= buf.readInt();
		this.ctime 		= buf.readInt();
		this.isWin 		= buf.readByte();
		byte size		= buf.readByte();
		for( int i = 0; i < size; i++ )
			awards.add( new AwardInfo(buf) );
		score 			= buf.readInt();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		super.buildTransformStream(buffer);
		buffer.writeByte( type );
		buffer.writeInt( chapterId );
		buffer.writeInt( ectypeId );
		buffer.writeInt( ctime );
	}
	
	@Override
	public boolean isComplete() {
		return (int) (System.currentTimeMillis()/1000) >= endtime;
	}
	
	@Override
	public void execut( FleetInfo fleet, Player player ) {
		// 发送奖励
		for( AwardInfo award : awards ){
			StarDepot depot = player.getDepots(fleet.getBerthSnid());
			depot.appendProp( award.getId(), award.getCount() );
		}
		// 计算评星奖励
		// TODO
		
		// 设置悬停
		fleet.setStatus( StatusType.HOVER.create() );
	}
	
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public int getChapterId() {
		return chapterId;
	}
	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}
	public int getEctypeId() {
		return ectypeId;
	}
	public void setEctypeId(int ectypeId) {
		this.ectypeId = ectypeId;
	}
	public int getEndtime() {
		return endtime;
	}
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	public byte getIsWin() {
		return isWin;
	}
	public void setIsWin(byte isWin) {
		this.isWin = isWin;
	}
	public List<AwardInfo> getAwards() {
		return awards;
	}
	public void setAwards(List<AwardInfo> awards) {
		this.awards = awards;
	}
	public int getCtime() {
		return ctime;
	}
	public void setCtime(int ctime) {
		this.ctime = ctime;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}


}
