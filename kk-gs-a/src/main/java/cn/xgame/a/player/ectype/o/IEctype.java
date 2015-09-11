package cn.xgame.a.player.ectype.o;

import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.EctypePo;

/**
 * 一个副本信息
 * @author deng		
 * @date 2015-9-10 下午4:25:58
 */
public class IEctype{
	
	private final EctypePo templet;
	
	
	public IEctype( int id ){
		this.templet	= CsvGen.getEctypePo(id);
	}

	public String toString(){
		return templet.id+"";
	}
	
	public int getNid() {
		return templet.id;
	}
	
	
}
