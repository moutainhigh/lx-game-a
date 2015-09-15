package cn.xgame.a.player.ectype.o;


import io.netty.buffer.ByteBuf;

import java.util.List;

import cn.xgame.a.IBufferStream;

import x.javaplus.collections.Lists;

/**
 * 一个星球的副本
 * @author deng		
 * @date 2015-9-10 下午6:28:30
 */
public class StarEctype implements IBufferStream{
	
	// 属于星球
	private final int snid;
	
	// 常规副本
	private List<ChapterEctype> general = Lists.newArrayList();
	
	// 普通限时副本
	private List<ChapterEctype> normal 	= Lists.newArrayList();
	
	public StarEctype( int id ){
		this.snid = id;
	}
	
	public String toString(){
		return snid + " - general=" + general.size() + ", normal=" + normal.size();
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeByte( general.size() );
		for( ChapterEctype ectype : general ){
			buf.writeInt( ectype.getNid() );
			buf.writeByte( ectype.getTimes() );
			ectype.putBuffer(buf);
		}
		buf.writeByte( normal.size() );
		for( ChapterEctype ectype : normal ){
			buf.writeInt( ectype.getNid() );
			buf.writeByte( ectype.getTimes() );
			buf.writeInt( ectype.getEndtime() );
			ectype.putBuffer(buf);
		}
	}

	@Override
	public void wrapBuffer( ByteBuf buf ) {
		byte count = buf.readByte();
		for( int j = 0; j < count; j++ ){
			ChapterEctype ectype = new ChapterEctype( snid, buf.readInt() );
			ectype.setTimes( buf.readByte() );
			ectype.wrapBuffer( buf );
			general.add(ectype);
		}
		count = buf.readByte();
		for( int j = 0; j < count; j++ ){
			ChapterEctype ectype = new ChapterEctype( snid, buf.readInt() );
			ectype.setTimes( buf.readByte() );
			ectype.setEndtime( buf.readInt() );
			ectype.wrapBuffer( buf );
			normal.add(ectype);
		}
	}
	
	public int getSnid(){ return snid; }

	public List<ChapterEctype> getGeneral() {
		return general;
	}
	public List<ChapterEctype> getNormal() {
		return normal;
	}

	public ChapterEctype getGeneralEctype( int id ) {
		for( ChapterEctype ectype : general ){
			if( ectype.getNid() == id )
				return ectype;
		}
		return null;
	}
	
	public ChapterEctype getNormalEctype( int id ) {
		for( ChapterEctype ectype : normal ){
			if( ectype.getNid() == id )
				return ectype;
		}
		return null;
	}

	/**
	 * 初始化常规副本次数
	 */
	public void initGeneralTimes() {
		for( ChapterEctype ectype : general )
			ectype.initTimes();
		for( ChapterEctype ectype : normal )
			ectype.initTimes();
	}

	/**
	 * 更新一下普通限时副本
	 */
	public void removeCrapNormalEctype() {
		List<ChapterEctype> removes = Lists.newArrayList();
		int curtime = (int) (System.currentTimeMillis()/1000);
		// 找出已经过期的副本
		for( ChapterEctype cha : normal ){
			if( curtime >= cha.getEndtime() )
				removes.add(cha);
		}
		// 然后统一删除掉
		normal.removeAll(removes);
	}

	/**
	 * 获取指定章节
	 * @param type
	 * @param cnid
	 * @param enid
	 * @return
	 */
	public ChapterEctype getChapter( int type, int cnid ) {
		return type == 2 ? getNormalEctype( cnid ) : getGeneralEctype( cnid );
	}

}
