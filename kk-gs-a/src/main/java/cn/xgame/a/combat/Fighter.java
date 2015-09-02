package cn.xgame.a.combat;

import java.util.ArrayList;
import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.combat.o.Answers;
import cn.xgame.a.combat.o.Askings;
import cn.xgame.a.combat.o.AtkAndDef;
import cn.xgame.a.player.captain.o.CaptainInfo;
import cn.xgame.a.player.ectype.IEctype;
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
	public List<AtkAndDef> 	attacks = new ArrayList<AtkAndDef>();
	
	// 防御属性列表
	public List<AtkAndDef> 	defends = new ArrayList<AtkAndDef>();
	
	// 问列表
	public List<Askings> 	askings = new ArrayList<Askings>();
	
	// 答列表
	public List<Answers> 	answers = new ArrayList<Answers>();
	
	// 装备总耐久度
	public int totalDur = 0;
	
	private AtkAndDef getAttacks( int type ) {
		for( AtkAndDef o : attacks ){
			if( o.type == type )
				return o;
		}
		return null;
	}
	private AtkAndDef getDefends(int type) {
		for( AtkAndDef o : defends ){
			if( o.type == type )
				return o;
		}
		return null;
	}
	private Askings getAskings(int id) {
		for( Askings o : askings ){
			if( o.id == id )
				return o;
		}
		return null;
	}
	private Answers getAnswers(int id) {
		for( Answers o : answers ){
			if( o.id == id )
				return o;
		}
		return null;
	}
	
	// 整理
	private void systemize() {
		List<AtkAndDef> a1 = Lists.newArrayList(attacks);
		List<AtkAndDef> a2 = Lists.newArrayList(defends);
		List<Askings> b1 = Lists.newArrayList(askings);
		List<Answers> b2 = Lists.newArrayList(answers);
		attacks.clear();
		defends.clear();
		askings.clear();
		answers.clear();
		
		for( AtkAndDef x : a1 ){
			AtkAndDef o = getAttacks( x.type );
			if( o == null ){
				attacks.add( x );
			}else{
				o.value += x.value;
			}
		}
		for( AtkAndDef x : a2 ){
			AtkAndDef o = getDefends( x.type );
			if( o == null ){
				defends.add( x );
			}else{
				o.value += x.value;
			}
		}
		for( Askings x : b1 ){
			Askings o = getAskings( x.id );
			if( o == null )
				askings.add( x );
		}
		for( Answers x : b2 ){
			Answers o = getAnswers( x.id );
			if( o == null )
				answers.add( x );
		}
	}
	
	/**
	 * 根据舰船塞入数据
	 * @param player
	 * @param ship
	 */
	public Fighter( Player player, ShipInfo ship ) {
		
		hp = ship.attr().getCurrentHp();
		// 
		totalDur = ship.warpFightProperty( attacks, defends, askings, answers );
		// 如果有舰长 那么还要塞入舰长的数据
		if( ship.getCaptainUID() != -1 ){
			CaptainInfo captain = player.getCaptains().getCaptain( ship.getCaptainUID() );
			totalDur += captain.warpFightProperty( attacks, defends, askings, answers );
		}
		
		// 自后整理一下 
		systemize();
	}
	

	/**
	 * 根据副本塞入数据
	 * @param ectype
	 */
	public Fighter( IEctype ectype ) {
		// 副本应答 - 问
		ectype.wrapAsking( askings );
		// 怪物数据
		hp = ectype.wrapEnemy( attacks, defends, askings, answers );
		// 自后整理一下 
		systemize();
	}

	
}
