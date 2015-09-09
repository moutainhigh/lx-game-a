package cn.xgame.a.player.ectype.enemy;


import java.util.List;

import x.javaplus.collections.Lists;
import cn.xgame.a.combat.o.AtkAndDef;
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
	
	// 攻击属性
	private List<AtkAndDef> atks 	= Lists.newArrayList();
	
	// 防御属性
	private List<AtkAndDef> defs 	= Lists.newArrayList();
	
	// 应答 - 问
	private List<Integer> askings = Lists.newArrayList();
	// 应答 - 答
	private List<Integer> answers = Lists.newArrayList();
	
	public Enemy(int id) {
		templet = CsvGen.getEnemyPo(id);
		initDropAward();
		initProperty();
		initAnswer();
	}

	private void initProperty() {
		if( !templet.atktype.isEmpty() && !templet.atkvalue.isEmpty() ){
			String[] a = templet.atktype.split(";");
			String[] b = templet.atkvalue.split(";");
			for( int i = 0; i < a.length; i++ ){
				atks.add( new AtkAndDef( Byte.parseByte( a[i] ), Float.parseFloat( b[i] )*count ) );
			}
		}
		if( !templet.deftype.isEmpty() && !templet.defvalue.isEmpty() ){
			String[] a = templet.deftype.split(";");
			String[] b = templet.defvalue.split(";");
			for( int i = 0; i < a.length; i++ ){
				defs.add( new AtkAndDef( Byte.parseByte( a[i] ), Float.parseFloat( b[i] )*count ) );
			}
		}
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
	
	private void initAnswer() {
		if( !templet.askings.isEmpty() ){
			String[] x = templet.askings.split( ";" );
			for( String o : x )
				askings.add( Integer.parseInt(o) );
		}
		if( !templet.answers.isEmpty() ){
			String[] x = templet.answers.split( ";" );
			for( String o : x )
				answers.add( Integer.parseInt(o) );
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
	public List<AtkAndDef> getAtks() {
		return atks;
	}
	public List<AtkAndDef> getDefs() {
		return defs;
	}
	public List<Integer> getAskings() {
		return askings;
	}
	public List<Integer> getAnswers() {
		return answers;
	}

}
