package cn.xgame.a.combat;

import java.util.ArrayList;
import java.util.List;

import cn.xgame.a.combat.o.Answers;
import cn.xgame.a.combat.o.Askings;
import cn.xgame.a.combat.o.AtkAndDef;
import cn.xgame.a.player.captain.o.CaptainInfo;
import cn.xgame.a.player.ectype.IEctype;
import cn.xgame.a.player.ectype.enemy.EnemyMeet;
import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.u.Player;

/**
 * 一个战斗者
 * @author deng
 * @date 2015-7-30 上午11:58:35
 */
public class Fighter {
	
	// 血量
	public int hp;
	
	// 攻击属性列表
	public List<AtkAndDef> attacks = new ArrayList<AtkAndDef>();
	
	// 防御属性列表
	public List<AtkAndDef> defends = new ArrayList<AtkAndDef>();
	
	// 问列表
	public List<Askings> askings = new ArrayList<Askings>();
	
	// 答列表
	public List<Answers> answers = new ArrayList<Answers>();
	
	
	
	/**
	 * 根据舰船塞入数据
	 * @param player 
	 * @param ship
	 */
	public Fighter( Player player, ShipInfo ship ) {
		// 
		hp = ship.warpFightProperty( attacks, defends, askings, answers );
		// 如果有舰长 那么还要塞入舰长的数据
		if( ship.getCaptainUID() != -1 ){
			CaptainInfo captain = player.getCaptains().getCaptain( ship.getCaptainUID() );
			hp += captain.warpFightProperty( attacks, defends, askings, answers );
		}
	}

	/**
	 * 根据副本塞入数据
	 * @param ectype
	 */
	public Fighter( IEctype ectype ) {
		hp = 100;
		// 副本应答 - 问
		ectype.wrapAsking( askings );
		// 怪物数据
		EnemyMeet enemyMeet = new EnemyMeet( ectype.template().meetId );
		hp = enemyMeet.wrapFightProperty( attacks, defends, askings, answers );
	}
	
}
