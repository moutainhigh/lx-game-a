package cn.xgame.a.player.ectype.enemy;


import java.util.List;

import x.javaplus.collections.Lists;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.EnemyPo;


/**
 * 一个怪物
 * @author deng		
 * @date 2015-7-30 下午10:31:50
 */
public class Enemy {

	private final EnemyPo templet;
	
	// 数量
	private int count 				= 1;
	
	// 奖励列表
	private List<DropAward> drops 	= Lists.newArrayList();
	
	
	public Enemy(int id) {
		templet = CsvGen.getEnemyPo(id);
		initDropAward();
	}
	
	private void initDropAward() {
		if( templet.rewards.isEmpty() )
			return;
		String[] content = templet.rewards.split("\\|");
		for( String str : content ){
			String[] x 		= str.split(";");
			DropAward drop 	= new DropAward( Integer.parseInt( x[0] ), Integer.parseInt( x[1] ), Integer.parseInt( x[2] ) );
			drops.add(drop);
		}
	}

	public EnemyPo templet(){ return templet; }

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<DropAward> getDrops() {
		return drops;
	}
	public int getHP() {
		return templet.hp*count;
	}
	
	public String getAtkType(){
		return templet.atktype;
	}
	public String getAtkValue() {
		String ret = "";
		String[] cotent = templet.atkvalue.split(";");
		for( String str : cotent ){
			int a = Integer.parseInt( str ) * count;
			ret += (a + ";");
		}
		return ret;
	}
	public String getDefType(){
		return templet.deftype;
	}
	public String getDefValue() {
		String ret = "";
		String[] cotent = templet.deftype.split(";");
		for( String str : cotent ){
			int a = Integer.parseInt( str ) * count;
			ret += (a + ";");
		}
		return ret;
	}
	
	
}
