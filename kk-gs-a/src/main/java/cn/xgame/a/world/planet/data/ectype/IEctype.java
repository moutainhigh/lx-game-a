package cn.xgame.a.world.planet.data.ectype;

import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Ectype;

/**
 * 一个副本
 * @author deng		
 * @date 2015-7-13 下午7:21:50
 */
public class IEctype {

	private final Ectype template;
	
	public IEctype( int id ) {
		template = CsvGen.getEctype(id);
	}

	public Ectype template(){ return template; }
	
	
}
