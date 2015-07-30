package cn.xgame.a.player.ectype;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.Util.Random;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.combat.o.Askings;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.AskingPo;
import cn.xgame.config.o.EctypePo;

/**
 * 副本基类
 * @author deng		
 * @date 2015-7-25 下午12:57:43
 */
public abstract class IEctype {
	
	protected final EctypePo template;
	
	// 所属星球ID
	public final int SNID ;
	
	// 副本类型
	protected EctypeType type;
	
	// 基础应答 - 问
	protected List<AskingPo> basisAskings = Lists.newArrayList();
	// 随机应答 - 问
	protected List<AskingPo> randomAskings = Lists.newArrayList();
	
	/**
	 * 从配置表获取
	 * @param id 所属星球ID
	 * @param src
	 */
	public IEctype( int id, EctypePo src ) {
		SNID 		= id;
		template 	= src;
		type		= EctypeType.fromNumber( template.type );
		randomAsking();
		initBasisAsking();
	}

	/**
	 * 从数据库 获取
	 * @param buffer
	 */
	public IEctype( ByteBuf buffer ){
		SNID 		= buffer.readInt();
		template 	= CsvGen.getEctypePo( buffer.readInt() );
		type		= EctypeType.fromNumber( template.type );
		byte size 	= buffer.readByte();
		for( int i = 0; i < size; i++ ){
			AskingPo o = CsvGen.getAskingPo( buffer.readInt() );
			randomAskings.add(o);
		}
		initBasisAsking();
	}

	/**
	 * 返回到数据库
	 * @param buffer
	 */
	public void putBuffer(ByteBuf buffer) {
		buffer.writeInt( SNID );
		buffer.writeInt( template.id );
		buffer.writeByte( randomAskings.size() );
		for( AskingPo o : randomAskings ){
			buffer.writeInt( o.id );
		}
	}

	/** 随机应答出来 */
	public void randomAsking() {
		randomAsking( template.ranswers );
		randomAsking( template.ianswers );
	}

	// 初始副本基础应答
	private void initBasisAsking() {
		if( template.eanswers.isEmpty() )
			return;
		String[] content = template.eanswers.split(";");
		for( String v : content ){
			AskingPo o = CsvGen.getAskingPo( Integer.parseInt( v ) );
			basisAskings.add( o );
		}
	}
	
	private void randomAsking( String answers ) {
		if( answers.isEmpty() )
			return;
		String[] content = answers.split("\\|");
		for( String x : content ){
			if( x.isEmpty() ) continue;
			String[] v = x.split(";");
			int rand = Random.get( 0, 10000 );
			if( rand > Integer.parseInt( v[1] ) ) 
				continue;
			AskingPo o = CsvGen.getAskingPo( Integer.parseInt( v[0] ) );
			randomAskings.add( o );
		}
	}
	
	/**
	 * 包装战斗 应答
	 * @param askings
	 */
	public void wrapAsking(List<Askings> askings) {
		List<AskingPo> all = Lists.newArrayList();
		all.addAll( basisAskings );
		all.addAll( randomAskings );
		for( AskingPo ask : all ){
			Askings o = new Askings(ask);
			askings.add(o);
		}
	}

	
	/**
	 * 该副本是否关闭
	 * @return
	 */
	public abstract boolean isClose();
	public EctypePo template(){ return template; }
	public int getNid() { return template.id; }
	public EctypeType type(){ return type ; }

	
}
