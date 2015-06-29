package cn.xgame.a.world.planet.ectype;

import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.config.o.Stars;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDto;

public class EctypePlanet extends IPlanet{

	public EctypePlanet(Stars clone) {
		super(clone);
	}

	@Override
	public void wrap(PlanetDataDto dto) {
		super.wrap(dto);
		
	}

	
	
	
	
	@Override
	public void updateDB() {
		
	}

}
