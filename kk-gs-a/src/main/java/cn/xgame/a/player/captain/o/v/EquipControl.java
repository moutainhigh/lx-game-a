package cn.xgame.a.player.captain.o.v;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.combat.CombatUtil;
import cn.xgame.a.combat.o.Answers;
import cn.xgame.a.combat.o.Askings;
import cn.xgame.a.combat.o.AtkAndDef;
import cn.xgame.a.prop.cequip.CEquipAttr;


/**
 * 装备库
 * @author deng		
 * @date 2015-7-24 下午6:16:37
 */
public class EquipControl implements IArrayStream,ITransformStream{

	// 装备
	private CEquipAttr equip = null;
	
	
	
	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		equip = new CEquipAttr( buf );
	}
	
	@Override
	public byte[] toBytes() {
		if( equip == null ) return null;
		ByteBuf buf = Unpooled.buffer( 1024 );
		equip.putBuffer( buf );
		return buf.array();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( equip == null ? -1 : equip.getUid() );
		if( equip != null ){
			equip.buildTransformStream(buffer);
		}
	}

	public CEquipAttr getEquip() { return equip; }
	public void setEquip( CEquipAttr equip ) { 
		this.equip = equip;
		this.equip.setUid( 1 );
	}

	/**
	 * 包装 战斗数据
	 * @param attacks
	 * @param defends
	 * @param askings
	 * @param answers
	 * @return
	 */
	public int warpFightProperty(List<AtkAndDef> attacks, List<AtkAndDef> defends, 
			List<Askings> askings, List<Answers> answers) {
		if( equip == null )
			return 0;
		CombatUtil.putAnswer( equip.templet().answers, answers );
		
		return 0;
	}


}
