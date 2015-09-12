package cn.xgame.a.player.ectype.o;

import io.netty.buffer.ByteBuf;

import java.util.List;

import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ChapterPo;

import x.javaplus.collections.Lists;
import x.javaplus.util.Util.Time;



/**
 * 一个章节副本
 * @author deng		
 * @date 2015-9-10 下午4:36:19
 */
public class ChapterEctype implements ITransformStream,IBufferStream{
	
	private final ChapterPo templet;
	private final int snid;
	
	// 剩余次数
	private byte times ;
	
	// 开启时间 暂时不用
//	private int starttime;
	// 结束时间
	private int endtime;
	
	// 副本列表
	private List<IEctype> ectypes = Lists.newArrayList();
	
	public ChapterEctype( int snid, int id ) {
		templet 	= CsvGen.getChapterPo(id);
		times		= templet.times;
		this.snid	= snid;
	}
	
	public String toString(){
		return templet.id + "- times=" + times + ", endtime=" + Time.refFormatDate(endtime*1000) + ", ectypes=" + ectypes;
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
	
	public int getSnid(){
		return snid;
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
	public int getEndtime() {
		return endtime;
	}
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	public List<IEctype> getEctypes() {
		return ectypes;
	}
	
	public IEctype getEctype( int enid ) {
		for( IEctype ectype : ectypes ){
			if( ectype.getNid() == enid )
				return ectype;
		}
		return null;
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
