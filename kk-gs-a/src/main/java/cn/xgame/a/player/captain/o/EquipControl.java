package cn.xgame.a.player.captain.o;

import java.util.List;

import cn.xgame.a.combat.CombatUtil;
import cn.xgame.a.combat.o.Answers;
import cn.xgame.a.combat.o.Askings;
import cn.xgame.a.combat.o.AtkAndDef;
import cn.xgame.a.prop.cequip.CEquip;


/**
 * 装备库
 * @author deng		
 * @date 2015-7-24 下午6:16:37
 */
public class EquipControl{

	private CEquip equip = null;
	
	public CEquip getEquip() {
		return equip;
	}
	public void setEquip(CEquip equip) {
		this.equip = equip;
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
		
		CombatUtil.putAnswer( equip.templet().answers, answers );
		
		return 0;
	}




}
