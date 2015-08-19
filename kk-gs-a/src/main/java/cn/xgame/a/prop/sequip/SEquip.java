package cn.xgame.a.prop.sequip;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
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
	
	// 当前耐久度
	private int currentDur = 0;
	
	public SEquip(int uid, int nid, int count) {
		super(uid, nid, count);
		templet = CsvGen.getWeaponPo(nid);
		currentDur = templet.dur;
	}
	
	public SEquip( PropsDto o ) {
		super(o);
		templet = CsvGen.getWeaponPo(getNid());
		if( o.getAttach() == null )
			return;
		ByteBuf buf = Unpooled.copiedBuffer( o.getAttach() );
		wrapAttach(buf);
	}

	@Override
	public IProp clone() {
		SEquip ret = new SEquip(getUid(), getNid(), getCount());
		ret.currentDur = currentDur;
		return ret;
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}
	
	public WeaponPo templet() { return templet; }

	@Override
	public void createDB(Player player) {
		ByteBuf buf = Unpooled.buffer( 1024 );
		putAttachBuffer(buf);
		super.create(player, buf.array());
	}
	@Override
	public void updateDB(Player player) {
		ByteBuf buf = Unpooled.buffer( 1024 );
		putAttachBuffer(buf);
		super.update(player, buf.array());
	}
	@Override
	public void putAttachBuffer(ByteBuf buf) {
		buf.writeInt(currentDur);
	}
	@Override
	public void wrapAttach(ByteBuf buf) {
		currentDur = buf.readInt();
	}

	/** 是否武器 */
	public boolean isWeapon() {
		return item().itemtype == 1;
	}
	/** 是否辅助装备 */
	public boolean isAssistEquip() {
		return item().itemtype == 2;
	}
	public int getCurrentDur() {
		return currentDur;
	}
	public void setCurrentDur(int currentDur) {
		this.currentDur = currentDur;
	}


	
}
