package cn.xgame.a.player.fleet.info.vote;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.classes.IVoteType;
import cn.xgame.net.netty.Netty.RW;

/**
 * 副本投票
 * @author deng		
 * @date 2015-10-30 下午3:20:00
 */
public class EctypeVote extends IVoteType{

	// 副本所属玩家UID 
	private String theirUID;
	
	// 副本类型 1.常规副本 2.普通限时 3.特殊限时
	private byte etype;
	
	// 章节ID
	private int chapterId;
	
	// 难度类型 1.普通本 2.挂机本
	private byte ltype;
	
	// 难度
	private byte level;
	
	public EctypeVote(String theirUID, byte etype, int chapterId, byte ltype, byte level) {
		super((byte)1);
		this.theirUID = theirUID;
		this.etype = etype;
		this.chapterId = chapterId;
		this.ltype = ltype;
		this.level = level;
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		super.buildTransformStream(buffer);
		RW.writeString(buffer, theirUID);
		buffer.writeByte(etype);
		buffer.writeInt(chapterId);
		buffer.writeByte(ltype);
		buffer.writeByte(level);
	}

	public String getTheirUID() {
		return theirUID;
	}
	public byte getEtype() {
		return etype;
	}
	public int getChapterId() {
		return chapterId;
	}
	public byte getLtype() {
		return ltype;
	}
	public byte getLevel() {
		return level;
	}
	
}
