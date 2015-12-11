package cn.xgame.a.player.manor.classes;

import cn.xgame.a.player.manor.info.BaseBuilding;
import cn.xgame.a.player.manor.info.IBuilding;
import cn.xgame.a.player.manor.info.PedleryBuilding;
import cn.xgame.a.player.manor.info.ProduceBuilding;
import cn.xgame.a.player.manor.info.ShopBuilding;
import cn.xgame.config.o.BbuildingPo;

/**
 * 建筑类型
 * @author deng		
 * @date 2015-12-11 下午1:47:45
 */
public enum BType {
	
	/**
	 * 基地类型
	 */
	BASE {
		@Override
		public IBuilding create(BbuildingPo templet) {
			return new BaseBuilding(BASE,templet);
		}
	},
	
	/**
	 * 产出类型
	 */
	PRODUCE {
		@Override
		public IBuilding create(BbuildingPo templet) {
			return new ProduceBuilding(PRODUCE,templet);
		}
	},
	
	/**
	 * 商店类型
	 */
	SHOP {
		@Override
		public IBuilding create(BbuildingPo templet) {
			return new ShopBuilding(SHOP,templet);
		}
	},
	
	/**
	 * 行商类型
	 */
	PEDIERY {
		@Override
		public IBuilding create(BbuildingPo templet) {
			return new PedleryBuilding(PEDIERY, templet);
		}
	};
	
	/**
	 * 根据类型创建对应建筑出来
	 * @param id
	 * @return
	 */
	public abstract IBuilding create(BbuildingPo templet);
}
