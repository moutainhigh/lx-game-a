package cn.xgame.a.player.ectype.classes;

import io.netty.buffer.ByteBuf;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IBufferStream;
import cn.xgame.a.fighter.o.Attackattr;

/**
 * 一个副本数据基类  - 以下数据将会保存数据库
 * @author deng		
 * @date 2015-10-27 下午8:26:24
 */
public class IEctype implements IBufferStream{

	// 难度
	private byte level;
	
	// 血量
	private int hp;
	
	// 攻击列表
	private List<Attackattr> atks = Lists.newArrayList();
	
	// 防御列表
	private List<Attackattr> defs = Lists.newArrayList();
	
	public IEctype( byte level ) {
		this.level = level;
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt(hp);
		buf.writeByte( atks.size() );
		for( Attackattr o : atks )
			o.putBuffer(buf);
		buf.writeByte( defs.size() );
		for( Attackattr o : defs )
			o.putBuffer(buf);
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		hp = buf.readInt();
		byte size = buf.readByte();
		for (int i = 0; i < size; i++) {
			Attackattr o = new Attackattr();
			o.wrapBuffer(buf);
			atks.add( o );
		}
		size = buf.readByte();
		for (int i = 0; i < size; i++) {
			Attackattr o = new Attackattr();
			o.wrapBuffer(buf);
			defs.add( o );
		}
	}
	
	public byte getLevel() {
		return level;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public List<Attackattr> getAtks() {
		return atks;
	}
	public void setAtks( byte type, float value ) {
		this.atks.add( new Attackattr(type, value) );
	}
	public List<Attackattr> getDefs() {
		return defs;
	}
	public void setDefs( byte type, float value ) {
		this.defs.add( new Attackattr(type, value) );
	}

	
}
