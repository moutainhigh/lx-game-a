package cn.xgame.a.player.fleet.info.status;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.fleet.classes.IResult;
import cn.xgame.a.player.fleet.classes.IStatus;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.fleet.info.result.Settlement;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.netty.Netty.RW;

/**
 * 战斗状态
 * @author deng		
 * @date 2015-9-11 上午12:39:04
 */
public class CombatStatus extends IStatus{
	
	// 所属副本玩家UID
	private String UID;
	
	// 副本类型 1.常规副本 2.普通限时 3.特殊限时
	private byte type;
	
	// 章节ID
	private int chapterId;
	
	// 难度类型 1.普通本 2.挂机本
	private byte ltype;
	
	// 难度
	private byte level;
	
	// 奖励列表
	private List<AwardInfo> awards = Lists.newArrayList();
	
	// 是否胜利
	private byte isWin;
	
	// 评分
	private int score;
	
	// 结果
	private IResult result;
	
	public CombatStatus() {
		super( StatusType.COMBAT );
	}
	
	@Override
	public void init( Object[] objects ) {
		int i = 0;
		UID			= (String) objects[i++];
		type 		= (Byte) objects[i++];
		chapterId 	= (Integer) objects[i++];
		ltype		= (Byte) objects[i++];
		level		= (Byte) objects[i++];
		isWin		= (Byte) objects[i++];
		List<?>	x	= (List<?>) objects[i++];
		for( Object o : x )
			awards.add( (AwardInfo) o );
		score		= (Integer) objects[i++];
		result		= (IResult) objects[i++];
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		RW.writeString(buf, UID);
		buf.writeByte( type );
		buf.writeInt( chapterId );
		buf.writeByte( ltype );
		buf.writeByte( level );
		buf.writeByte( isWin );
		buf.writeByte( awards.size() );
		for( AwardInfo award : awards )
			award.buildTransformStream(buf);
		buf.writeInt( score );
		buf.writeByte( result.type() );
		result.putBuffer(buf);
	}
	
	@Override
	public void wrapBuffer(ByteBuf buf) {
		this.UID		= RW.readString(buf);
		this.type 		= buf.readByte();
		this.chapterId 	= buf.readInt();
		this.ltype 		= buf.readByte();
		this.level 		= buf.readByte();
		this.isWin 		= buf.readByte();
		byte size		= buf.readByte();
		for( int i = 0; i < size; i++ )
			awards.add( new AwardInfo(buf) );
		score 			= buf.readInt();
		byte rtype		= buf.readByte();
		result			= IResult.create(rtype, buf);
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		super.buildTransformStream(buffer);
		RW.writeString(buffer, UID);
		buffer.writeByte( type );
		buffer.writeInt( chapterId );
		buffer.writeByte( ltype );
		buffer.writeByte( level );
		result.buildTransformStream( buffer );
	}
	
	@Override
	public boolean isComplete() {
		return result.isComplete();
	}
	
	@Override
	public void update( FleetInfo fleet, Player player ) {
		
		// 如果是战斗中 那么就设置成 结算中即可
		if( result.type() == 1 ){
			result = new Settlement( result.getCtime() );
		}
		
	}
	
	public byte getType() {
		return type;
	}
	public int getChapterId() {
		return chapterId;
	}
	public byte getIsWin() {
		return isWin;
	}
	public List<AwardInfo> getAwards() {
		return awards;
	}
	public int getScore() {
		return score;
	}
	public byte getLtype() {
		return ltype;
	}
	public byte getLevel() {
		return level;
	}
	public String getUID() {
		return UID;
	}
	public IResult getResult() {
		return result;
	}
	public void setResult(IResult result) {
		this.result = result;
	}

}
