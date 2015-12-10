package cn.xgame.a.player.ectype.info;

import io.netty.buffer.ByteBuf;

import java.util.List;

import cn.xgame.a.IBufferStream;

import x.javaplus.collections.Lists;



/**
 * 一个星球的常规副本 难度记录
 * @author deng		
 * @date 2015-12-10 下午3:20:12
 */
public class StarGeneralEctype implements IBufferStream{
	
	private int snid;
	
	private List<ChapterLevel> cls = Lists.newArrayList();
	
	public StarGeneralEctype( int id ){
		this.snid = id;
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt( cls.size() );
		for( ChapterLevel cl : cls ){
			buf.writeInt( cl.id );
			buf.writeByte( cl.size );
			buf.writeByte( cl.curIndex );
		}
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			ChapterLevel cl = new ChapterLevel();
			cl.id = buf.readInt();
			cl.size = buf.readByte();
			cl.curIndex = buf.readByte();
			cls.add(cl);
		}
	}
	
	public int getSnid() {
		return snid;
	}

	public ChapterLevel getCl( int id ){
		for( ChapterLevel cl : cls ){
			if( cl.id == id )
				return cl;
		}
		return null;
	}
	
	public void put( int id, int size, int index ){
		ChapterLevel cl = new ChapterLevel();
		cl.id = id;
		cl.size = (byte) size;
		cl.curIndex = (byte) index;
		cls.add(cl);
	}


	
}
