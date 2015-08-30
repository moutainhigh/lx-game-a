package cn.xgame.a.prop.sequip;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.WeaponPo;
import cn.xgame.gen.dto.MysqlGen.PropsDto;

/**
 * 舰船装备属性
 * @author deng		
 * @date 2015-6-18 下午1:49:18
 */
public class SEquipAttr extends IProp{

	private final WeaponPo templet;
	
	// 当前耐久度
	private int currentDur = 0;
	
	public SEquipAttr(int uid, int nid, int count) {
		super(uid, nid, count);
		templet 	= CsvGen.getWeaponPo(nid);
		currentDur 	= templet.dur;
	}
	
	public SEquipAttr( PropsDto o ) {
		super(o);
		templet = CsvGen.getWeaponPo(getNid());
	}

	@Override
	public IProp clone() {
		SEquipAttr ret = new SEquipAttr(getUid(), getNid(), getCount());
		ret.currentDur = this.currentDur;
		return ret;
	}
	
	public WeaponPo templet() { return templet; }

	@Override
	public byte[] toAttachBytes() {
		ByteBuf buf = Unpooled.buffer( 4 );
		buf.writeInt( currentDur );
		return buf.array();
	}

	@Override
	public void wrapAttachBytes(byte[] bytes) {
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		currentDur 	= buf.readInt();
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
