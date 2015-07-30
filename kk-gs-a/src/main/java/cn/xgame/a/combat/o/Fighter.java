package cn.xgame.a.combat.o;

import java.util.ArrayList;
import java.util.List;

import cn.xgame.a.player.ectype.IEctype;
import cn.xgame.a.player.ship.o.ShipInfo;

/**
 * 一个战斗者
 * @author deng		
 * @date 2015-7-30 上午11:58:35
 */
public class Fighter {
	
	// 血量
	public int hp;
	
	// 初始战斗时间
	public int initFightTime;
	
	// 攻击属性列表
	public List<AtkAndDef> attacks = new ArrayList<AtkAndDef>();
	
	// 防御属性列表
	public List<AtkAndDef> defends = new ArrayList<AtkAndDef>();
	
	// 问列表
	public List<Asking> askings = new ArrayList<Asking>();
	
	// 答列表
	public List<Answer> answers = new ArrayList<Answer>();
	
	
	
	/**
	 * 根据舰船塞入数据
	 * @param ship
	 */
	public Fighter( ShipInfo ship ) {
		hp = ship.template().hp;
		initFightTime = 0;
		ship.getEquips().warpFightProperty( attacks, defends );
	}

	/**
	 * 根据副本塞入数据
	 * @param ectype
	 */
	public Fighter( IEctype ectype ) {
		hp = 100;
		initFightTime = ectype.template().btime;
		
	}
	
}
