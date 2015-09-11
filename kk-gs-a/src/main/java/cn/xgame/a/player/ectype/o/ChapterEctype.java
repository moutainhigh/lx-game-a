package cn.xgame.a.player.ectype.o;

import io.netty.buffer.ByteBuf;

import java.util.List;

import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ChapterPo;

import x.javaplus.collections.Lists;



/**
 * 一个章节副本
 * @author deng		
 * @date 2015-9-10 下午4:36:19
 */
public class ChapterEctype implements ITransformStream,IBufferStream{
	
	private final ChapterPo templet;
	
	// 剩余次数
	private byte times ;
	
	// 记录时间
	private int rtime;
	private int persistTime;
	
	// 副本列表
	private List<IEctype> ectypes = Lists.newArrayList();
	
	public ChapterEctype( int id ) {
		templet 	= CsvGen.getChapterPo(id);
		times		= templet.times;
	}
	
	public String toString(){
		return templet.id + "- times=" + times + ", rtime=" + getEndTime() + ", ectypes=" + ectypes;
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeByte( ectypes.size() );
		for( IEctype o : ectypes )
			buf.writeInt( o.getNid() );
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ )
			ectypes.add( new IEctype( buf.readInt() ) );
	}

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeInt( templet.id );
		buffer.writeByte( times );
	}
	
	public int getNid() {
		return templet.id;
	}
	public byte getTimes() {
		return times;
	}
	public void setTimes(byte times) {
		this.times = times;
	}
	public int getRtime() {
		return rtime;
	}
	public void setRtime(int rtime) {
		this.rtime = rtime;
	}
	public int getPersistTime() {
		return persistTime;
	}
	public int getEndTime() {
		return rtime + persistTime;
	}
	public void setPersistTime(int persistTime) {
		this.persistTime = persistTime;
	}
	public List<IEctype> getEctypes() {
		return ectypes;
	}
	
	/**
	 * 生成下一个副本
	 */
	public void generateNextEctype() {
		int enid = getEctypeToIndex( ectypes.size() );
		if( enid == 0 ) return;
		ectypes.add( new IEctype(enid) );
	}

	// 根据下标获取对应难得的副本ID
	private int getEctypeToIndex( int index ) {
		if( templet.node.isEmpty() ) return 0;
		String[] str = templet.node.split(";");
		if( index >= str.length ) index = str.length;
		return Integer.parseInt( str[index] );
	}

	/** 初始化次数 */
	public void initTimes() {
		times = templet.times;
	}



	
}
