package cn.xgame.a.prop.cequip;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Weapon;
import cn.xgame.gen.dto.MysqlGen.M_cequipDto;

/**
 * 舰长装备对象
 * @author deng		
 * @date 2015-6-18 下午1:49:59
 */
public class CEquip extends IProp{

	private final Weapon templet;
	
	/**
	 * 从数据库获取
	 * @param o
	 */
	public static CEquip wrapDB( M_cequipDto o ) {
		CEquip ret = new CEquip( o.getUid(), o.getNid(), o.getCount() );
		return ret;
	}
	
	public CEquip(int uid, int nid, int count) {
		super(uid, nid, count);
		templet = CsvGen.getWeapon(nid);
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

	
	public Weapon templet() { return templet; }
	
}
