package cn.xgame.a.player.fleet.purpose;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.other.IPurpose;

/**
 * 打副本
 * @author deng		
 * @date 2015-9-14 下午12:27:03
 */
public class FightEctype extends IPurpose{
	
	// 副本类型 1.常规副本 2.普通限时 3.特殊限时
	private byte type;
	
	// 章节ID
	private int chapterId;
	
	// 副本ID
	private int ectypeId;
	
	public FightEctype( byte _type ) {
		super( _type );
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeByte( type );
		buf.writeInt( chapterId );
		buf.writeInt( ectypeId );
	}
	
	@Override
	public void wrapBuffer(ByteBuf buf) {
		this.type 		= buf.readByte();
		this.chapterId 	= buf.readInt();
		this.ectypeId 	= buf.readInt();		
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		putBuffer(buffer);
		buffer.writeByte( type );
		buffer.writeInt( chapterId );
		buffer.writeInt( ectypeId );
	}

	public int getEctypeId() {
		return ectypeId;
	}
	public void setEctypeId(int ectypeId) {
		this.ectypeId = ectypeId;
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




}
