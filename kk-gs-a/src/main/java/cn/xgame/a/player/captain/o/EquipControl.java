package cn.xgame.a.player.captain.o;

import cn.xgame.a.player.IHold;
import cn.xgame.a.prop.IProp;

/**
 * 装备库
 * @author deng		
 * @date 2015-7-24 下午6:16:37
 */
public class EquipControl extends IHold{

	@Override
	public boolean roomIsEnough(IProp prop) {
		return false;
	}

}
