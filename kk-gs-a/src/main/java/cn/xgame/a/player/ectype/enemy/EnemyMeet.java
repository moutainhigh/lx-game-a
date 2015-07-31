package cn.xgame.a.player.ectype.enemy;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.combat.CombatUtil;
import cn.xgame.a.combat.o.Answers;
import cn.xgame.a.combat.o.Askings;
import cn.xgame.a.combat.o.AtkAndDef;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.MeetPo;
import cn.xgame.utils.Logs;

/**
 * 敌人集合
 * @author deng		
 * @date 2015-7-30 下午10:26:40
 */
public class EnemyMeet {

	private final MeetPo templet;
	
	// 敌人列表
	private List<Enemy> enemys = Lists.newArrayList();
	
	// 奖励列表
	private List<DropAward> drop = Lists.newArrayList();
	
	public EnemyMeet( int id ){
		templet = CsvGen.getMeetPo(id);
		initEnemy();
		initAward();
	}
	public MeetPo templet(){ return templet; }
	public List<Enemy> getEnemys(){ return enemys; }
	public List<DropAward> getAwards(){ return drop; }
	
	
	// 初始化 怪物
	private void initEnemy() {
		try {
			
			String[] ships 		= templet.ships.split(";"); //敌人ID
			String[] counts 	= templet.count.split(";"); //数量
			String[] lvs		= templet.lvs.split(";");	//等级
			String[] equips		= templet.equips.split("\\|");//装备
			
			for( int i = 0; i < ships.length; i++ ){
				
				Enemy enemy = new Enemy( Integer.parseInt( ships[i] ) );
				enemy.setCount( Integer.parseInt( counts[i] ) );
				enemy.setLv( Short.parseShort( lvs[i] ) );
				if( equips.length != 0 )
					enemy.wrapEquips( equips[i] );
				enemys.add(enemy);
			}
			
		} catch (Exception e) {
			Logs.error( "EnemyMeet.initEnemy" , e );
		}
	}
	
	//初始化 奖励
	private void initAward() {
		if( templet.rewards.isEmpty() )
			return ;
		String[] content = templet.rewards.split("\\|");
		for( String x : content ){
			if( x.isEmpty() ) continue;
			String[] v = x.split(";");
			int id 		= Integer.parseInt( v[0] );
			int count 	= Integer.parseInt( v[1] );
			int rand 	= v.length >= 3 ? Integer.parseInt( v[2] ) : 10000;
			DropAward o = new DropAward( id, count, rand );
			drop.add(o);
		}
	}
	
	/**
	 * 包装战斗数据
	 * @param attacks
	 * @param defends
	 * @param askings
	 * @param answers
	 * @return
	 */
	public int wrapFightProperty(List<AtkAndDef> attacks, List<AtkAndDef> defends, 
			List<Askings> askings, List<Answers> answers) {
		
		int hp = 0;
		for( Enemy enemy : enemys ){
			// 血量
			hp += enemy.templet().HP;
			// 基础属性
			CombatUtil.putBasisProperty( attacks, defends, enemy.templet().type, enemy.templet().value );
			// 问
			CombatUtil.putAsking( enemy.templet().askings, askings );
			// 答
		}
		
		return hp;
	}
	
}
