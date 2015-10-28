package cn.xgame.a.player.ectype.classes;

import io.netty.buffer.ByteBuf;

import java.util.List;

import x.javaplus.collections.Arrays;
import x.javaplus.collections.Lists;

import cn.xgame.a.IBufferStream;
import cn.xgame.a.player.ectype.info.EctypeInfo;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.AskingPo;
import cn.xgame.config.o.ChapterPo;
import cn.xgame.config.o.EctypePo;
import cn.xgame.config.o.RquestionPo;
import cn.xgame.utils.Logs;

/**
 * 章节基类 - 以下数据将会保存数据库
 * @author deng		
 * @date 2015-10-27 下午6:10:57
 */
public class IChapter implements IBufferStream{
	
	// 副本所属星球
	private final int 			snid;
	// 章节ID
	private final int 			id;
	// 副本模板ID
	private int 				tempId;
	
	// 剩余次数  -1表示无限次
	private byte 				times ;
	
	// 结束时间  0表示无限制
	private int 				endtime;
	
	// 应答列表
	private List<Integer> 		questions = Lists.newArrayList();
	
	// 副本关卡列表
	private List<EctypeInfo> 	ectypes = Lists.newArrayList();
	
	
	public IChapter( int id, int snid, int tempId ){
		this.id 	= id;
		this.snid 	= snid;
		this.tempId = tempId;
	}
	public void init( ChapterPo templet ) {
		times 	= templet.times == 0 ? -1 : templet.times;
		initQuestions( templet.qc, templet.qp );
		generateEctype( templet.nquality.isEmpty() ? 1 : Integer.parseInt( templet.nquality.split( ";" )[0] ) );
	}
	// 初始应答
	private void initQuestions( String count, String pool ) {
		if( count.isEmpty() || pool.isEmpty() )
			return;
		String[] counts = count.split("\\|");
		String[] pools 	= pool.split( "\\|" );
		if( counts.length != pools.length )
			return ;
		for( int i = 0; i < counts.length; i++ ){
			RquestionPo templet = CsvGen.getRquestionPo( Integer.parseInt(pools[i]) );
			if( templet == null ){
				Logs.error( "在生成应答的时候 Rquestion表格找不到 " + pools[i] + ", at = ChapterInfo.generateQuestions" );
				continue;
			}
			
			List<String> str = Arrays.selectNumberToList( Integer.parseInt(counts[i]), templet.question.split(";") );
			for( String x : str ){
				AskingPo asking = CsvGen.getAskingPo( Integer.parseInt(x) );
				if( asking == null ) continue;
				questions.add( asking.id );
			}
		}
	}
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt(tempId);
		buf.writeByte(times);
		buf.writeInt(endtime);
		buf.writeByte( questions.size() );
		for( int id : questions )
			buf.writeInt(id);
		buf.writeByte( ectypes.size() );
		for( EctypeInfo o : ectypes ){
			buf.writeByte( o.getLevel() );
			o.putBuffer(buf);
		}
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		tempId 		= buf.readInt();
		times 		= buf.readByte();
		endtime 	= buf.readInt();
		EctypePo templet = CsvGen.getEctypePo( tempId );
		byte size = buf.readByte();
		for (int i = 0; i < size; i++) 
			questions.add( buf.readInt() );
		size = buf.readByte();
		for (int i = 0; i < size; i++) {
			EctypeInfo o = new EctypeInfo( buf.readByte(), templet );
			o.wrapBuffer(buf);
			ectypes.add( o );
		}
	}
	
	/**
	 * 根据难度生成一个副本数据
	 * @param level
	 */
	public void generateEctype( int level ) {
		EctypePo templet = CsvGen.getEctypePo( tempId );
		if( templet == null ) 
			return;
		EctypeInfo ectype = new EctypeInfo( (byte) level, templet );
		ectype.setAttribute( templet );
		ectypes.add( ectype );
	}
	
	public int getId() {
		return id;
	}
	public int getSnid() {
		return snid;
	}
	public byte getTimes() {
		return times;
	}
	public int getEndtime() {
		return endtime;
	}
	public void setEndtime( int endtime ) {
		this.endtime = endtime;
	}
	public List<Integer> getQuestions() {
		return questions;
	}
	public List<EctypeInfo> getEctypes() {
		return ectypes;
	}
	public int getTempId() {
		return tempId;
	}

}
