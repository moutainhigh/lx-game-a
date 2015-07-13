package cn.xgame.a.world.planet.data.ectype;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Ectypelist;

/**
 * 一个副本列表
 * @author deng		
 * @date 2015-7-13 下午6:16:09
 */
public class EctypeLists implements ITransformStream {

	private final Ectypelist template;
	
	private List<IEctype> ectypes = Lists.newArrayList();
	
	
	public EctypeLists( int id ){
		template = CsvGen.getEctypelist(id);
		if( template.list == null || template.list.isEmpty() )
			return;
		String[] content = template.list.split(";");
		for( String x : content ){
			if( x.isEmpty() ) continue;
			IEctype e = new IEctype( Integer.parseInt(x) );
			ectypes.add( e );
		}
	}

	public Ectypelist template(){ return template; }
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeInt( template.id );
	}

}
