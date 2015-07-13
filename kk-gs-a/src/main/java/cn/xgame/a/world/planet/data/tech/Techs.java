package cn.xgame.a.world.planet.data.tech;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Tech;

/**
 * 科技
 * @author deng		
 * @date 2015-6-30 下午5:51:13
 */
public class Techs implements ITransformStream {

	private final Tech templet;
	
	public Techs( int id ) {
		templet = CsvGen.getTech(id);
	}
	
	public Techs( Techs src ){
		templet = src.templet;
	}
	
	public Tech templet() { return templet; }

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( templet.id );
	}


}
