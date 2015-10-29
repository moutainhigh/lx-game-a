package cn.xgame.a.player.fleet.info.purpose;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.classes.IPurpose;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;

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
	
	// 难度类型 1.普通本 2.挂机本
	private byte ltype;
	
	// 难度
	private byte level;
	
	/**
	 * 创建一个 打副本 的目的
	 * @param etype 副本类型 
	 * @param cnid 章节ID
	 * @param ltype 难度类型 1.普通本 2.挂机本
	 * @param level 难度
	 */
	public FightEctype( byte etype, int cnid, byte ltype, byte level ) {
		super( (byte) 1 );
		this.etype 		= etype;
		this.chapterId 	= cnid;
		this.ltype		= ltype;
		this.level 		= level;
	}
	public FightEctype() {
		super( (byte) 1 );
	}

	@Override
	public void putBuffer( ByteBuf buf ) {
		buf.writeByte( etype );
		buf.writeInt( chapterId );
		buf.writeByte( ltype );
		buf.writeByte( level );
	}
	
	@Override
	public void wrapBuffer(ByteBuf buf) {
		this.etype 		= buf.readByte();
		this.chapterId 	= buf.readInt();
		this.ltype		= buf.readByte();
		this.level 		= buf.readByte();
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( type() );
		putBuffer( buffer );
	}

	@Override
	public void execut(  int starttime, int continutime, int targetId, FleetInfo fleet, Player player) {
		
		
		fleet.setBerthSnid( targetId );
		// 改变战斗状态
		fleet.changeStatus( StatusType.COMBAT );
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
	public void setLtype(byte ltype) {
		this.ltype = ltype;
	}
	public byte getLevel() {
		return level;
	}
	public void setLevel(byte level) {
		this.level = level;
	}

}
