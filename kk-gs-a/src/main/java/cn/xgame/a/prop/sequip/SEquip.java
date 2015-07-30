package cn.xgame.a.prop.sequip;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.WeaponPo;
import cn.xgame.gen.dto.MysqlGen.PropsDto;

/**
 * 舰船装备对象
 * @author deng		
 * @date 2015-6-18 下午1:49:18
 */
public class SEquip extends IProp{

	private final WeaponPo templet;
	
	public SEquip(int uid, int nid, int count) {
		super(uid, nid, count);
		templet = CsvGen.getWeaponPo(nid);
	}
	
	public SEquip( PropsDto o ) {
		super(o);
		templet = CsvGen.getWeaponPo(getnId());
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
	
	public WeaponPo templet() { return templet; }
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

	@Override
	public void putAttachBuffer(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wrapAttach(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	/** 是否武器 */
	public boolean isWeapon() {
		return item().itemtype == 1;
	}
	/** 是否辅助装备 */
	public boolean isAssistEquip() {
		return item().itemtype == 2;
	}


	
}
