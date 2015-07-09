package cn.xgame.a.prop.sequip;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Weapon;
import cn.xgame.gen.dto.MysqlGen.PropsDto;

/**
 * 舰船装备对象
 * @author deng		
 * @date 2015-6-18 下午1:49:18
 */
public class SEquip extends IProp{

	private final Weapon templet;
	
	public SEquip(int uid, int nid, int count) {
		super(uid, nid, count);
		templet = CsvGen.getWeapon(nid);
	}
	
	public SEquip( PropsDto o ) {
		super(o);
		templet = CsvGen.getWeapon(getnId());
	}

	@Override
	public IProp clone() {
		SEquip ret = new SEquip(getuId(), getnId(), getCount());
		return ret;
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}
	
	public Weapon templet() { return templet; }
	@Override
	public PropType type() { return PropType.SEQUIP; }

	@Override
	public void createDB(Player player) {
		super.create(player, null);
	}

	@Override
	public void updateDB(Player player) {
		super.update(player, null);
	}


	
}
