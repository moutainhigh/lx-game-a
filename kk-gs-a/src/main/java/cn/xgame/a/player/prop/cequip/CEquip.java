package cn.xgame.a.player.prop.cequip;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.prop.IProp;
import cn.xgame.a.player.prop.PropType;
import cn.xgame.a.player.u.Player;

/**
 * 舰长装备对象
 * @author deng		
 * @date 2015-6-18 下午1:49:59
 */
public class CEquip extends IProp{

	public CEquip(int uid, int nid, int count) {
		initialize(uid, nid, count);
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PropType type() {
		return PropType.CEQUIP;
	}

	@Override
	public void createDB(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDB(Player player) {
		// TODO Auto-generated method stub
		
	}

}
