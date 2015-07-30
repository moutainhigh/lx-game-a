package cn.xgame.a.player.ectype.enemy;

import java.util.List;

import cn.xgame.a.combat.o.Answers;
import cn.xgame.a.combat.o.Askings;
import cn.xgame.a.combat.o.AtkAndDef;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.MeetPo;

/**
 * 敌人集合
 * @author deng		
 * @date 2015-7-30 下午10:26:40
 */
public class EnemyMeet {

	private final MeetPo templet;
	
	// 敌人列表
	
	
	
	public EnemyMeet( int id ){
		templet = CsvGen.getMeetPo(id);
	}
	
	public MeetPo templet(){ return templet; }

	/**
	 * 包装战斗数据
	 * @param attacks
	 * @param defends
	 * @param askings
	 * @param answers
	 * @return
	 */
	public int warpFightProperty(List<AtkAndDef> attacks, List<AtkAndDef> defends, 
			List<Askings> askings, List<Answers> answers) {
		
		
		
		
		
		return 0;
	}
	
}
