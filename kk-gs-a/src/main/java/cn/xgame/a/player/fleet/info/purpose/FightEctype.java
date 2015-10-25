package cn.xgame.a.player.fleet.info.purpose;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.classes.IPurpose;

/**
 * 打副本
 * @author deng		
 * @date 2015-9-14 下午12:27:03
 */
public class FightEctype extends IPurpose{
	
	// 副本类型 1.常规副本 2.普通限时 3.特殊限时
	private byte etype;
	
	// 章节ID
	private int chapterId;
	
	// 副本ID
	private int ectypeId;
	
	public FightEctype( byte etype, int cnid, int enid ) {
		super( (byte) 1 );
		this.etype 		= etype;
		this.chapterId 	= cnid;
		this.ectypeId 	= enid;
	}
	public FightEctype() {
		super( (byte) 1 );
	}

	@Override
	public void putBuffer( ByteBuf buf ) {
		buf.writeByte( etype );
		buf.writeInt( chapterId );
		buf.writeInt( ectypeId );
	}
	
	@Override
	public void wrapBuffer(ByteBuf buf) {
		this.etype 		= buf.readByte();
		this.chapterId 	= buf.readInt();
		this.ectypeId 	= buf.readInt();		
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( type() );
		putBuffer( buffer );
	}

	public int getEctypeId() {
		return ectypeId;
	}
	public byte getEtype() {
		return etype;
	}
	public int getChapterId() {
		return chapterId;
	}

}
