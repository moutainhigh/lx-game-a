package cn.xgame.a.world.planet.ectype;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.config.o.StarsPo;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDto;

/**
 * 中转星
 * @author deng		
 * @date 2015-6-18 下午4:24:05
 */
public class EctypePlanet extends IPlanet{

	public EctypePlanet(StarsPo clone) {
		super(clone);
	}

	@Override
	public void wrap(PlanetDataDto dto) {
		super.wrap(dto);
		
	}

	
	
	
	
	@Override
	public void updateDB() {
		
	}
	
	
	@Override
	public List<Integer> getScopePlanet() {
		return Lists.newArrayList();
	}



}
