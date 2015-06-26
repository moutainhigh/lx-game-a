package cn.xgame.a.world.planet.entrepot;

import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.config.o.Stars;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDto;

public class EntrepotPlanet extends IPlanet{

	public EntrepotPlanet(Stars clone) {
		super(clone);
	}

	@Override
	public void wrap(PlanetDataDto dto) {
		super.wrap(dto);
		
	}


}
