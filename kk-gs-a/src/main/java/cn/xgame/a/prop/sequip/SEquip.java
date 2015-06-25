package cn.xgame.a.prop.sequip;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;

/**
 * 舰船装备对象
 * @author deng		
 * @date 2015-6-18 下午1:49:18
 */
public class SEquip extends IProp{

	public SEquip(int uid, int nid, int count) {
		initialize(uid, nid, count);
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PropType type() {
		return PropType.SEQUIP;
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
