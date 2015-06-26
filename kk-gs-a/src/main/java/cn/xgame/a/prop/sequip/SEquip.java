package cn.xgame.a.prop.sequip;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.gen.dto.MysqlGen.M_sequipDto;

/**
 * 舰船装备对象
 * @author deng		
 * @date 2015-6-18 下午1:49:18
 */
public class SEquip extends IProp{

	/**
	 * 从数据库获取
	 * @param o
	 */
	public static SEquip wrapDB( M_sequipDto o ) {
		SEquip ret = new SEquip( o.getUid(), o.getNid(), o.getCount() );
		return ret;
	}
	
	public SEquip(int uid, int nid, int count) {
		super(uid, nid, count);
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
