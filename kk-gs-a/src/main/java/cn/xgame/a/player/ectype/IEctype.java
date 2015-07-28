package cn.xgame.a.player.ectype;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.Util.Random;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.answering.Questions;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Ectype;
import cn.xgame.config.o.Question;

/**
 * 副本基类
 * @author deng		
 * @date 2015-7-25 下午12:57:43
 */
public abstract class IEctype {
	
	protected final Ectype template;
	
	// 所属星球ID
	protected final int snid ;
	
	// 副本类型
	protected EctypeType type;
	
	// 随机应答 - 提问
	protected List<Questions> questions = Lists.newArrayList();
	
	/**
	 * 从配置表获取
	 * @param id 所属星球ID
	 * @param src
	 */
	public IEctype( int id, Ectype src ) {
		snid 		= id;
		template 	= src;
		type		= EctypeType.fromNumber( template.type );
		randomQuestions();
	}
	
	/**
	 * 从数据库 获取
	 * @param buffer
	 */
	public IEctype( ByteBuf buffer ){
		snid 		= buffer.readInt();
		template 	= CsvGen.getEctype( buffer.readInt() );
		type		= EctypeType.fromNumber( template.type );
		byte size 	= buffer.readByte();
		for( int i = 0; i < size; i++ ){
			Questions o = new Questions( buffer.readInt() );
			questions.add(o);
		}
	}

	/**
	 * 返回到数据库
	 * @param buffer
	 */
	public void putBuffer(ByteBuf buffer) {
		buffer.writeInt( snid );
		buffer.writeInt( template.id );
		buffer.writeByte( questions.size() );
		for( Questions o : questions ){
			buffer.writeInt( o.getNid() );
		}
	}

	/** 随机应答出来 */
	public void randomQuestions() {
//		if( template.ranswers.isEmpty() )
//			return;
//		String[] content = template.ranswers.split("\\|");
//		for( String x : content ){
//			if( x.isEmpty() ) continue;
//			String[] v = x.split(";");
//			int rand = Random.get( 0, 10000 );
//			if( rand > Integer.parseInt( v[1] ) ) 
//				continue;
//			Questions o = new Questions( Integer.parseInt( v[0] ) );
//			questions.add( o );
//		}
		// 随机一个出来
		List<Question> ls = CsvGen.questions;
		int idx = Random.get( 0, ls.size() -1 );
		int id = ls.get(idx).id;
		// 放入应答列表
		Questions o = new Questions( id );
		questions.add( o );
	}
	
	/**
	 * 该副本是否关闭
	 * @return
	 */
	public abstract boolean isClose();
	public Ectype template(){ return template; }
	public int getNid() { return template.id; }
	public EctypeType type(){ return type ; }
	
}
