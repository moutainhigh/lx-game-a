package cn.xgame.a.player.ectype.o;


import java.util.List;

import x.javaplus.collections.Lists;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.fighter.o.Attackattr;
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
	
	// 攻击属性
	private List<Attackattr> atks 	= Lists.newArrayList();
	
	// 防御属性
	private List<Attackattr> defs 	= Lists.newArrayList();
	
	// 应答 - 问
	private List<Integer> askings 	= Lists.newArrayList();
	// 应答 - 答
	private List<Integer> answers 	= Lists.newArrayList();
	
	public Enemy( int id, int count ) {
		this.templet 	= CsvGen.getEnemyPo(id);
		this.count 		= count;
		initProperty();
		initAnswer();
	}

	private void initProperty() {
		if( !templet.atktype.isEmpty() && !templet.atkvalue.isEmpty() ){
			String[] a = templet.atktype.split(";");
			String[] b = templet.atkvalue.split(";");
			for( int i = 0; i < a.length; i++ ){
				atks.add( new Attackattr( Byte.parseByte( a[i] ), Float.parseFloat( b[i] )*count ) );
			}
		}
		if( !templet.deftype.isEmpty() && !templet.defvalue.isEmpty() ){
			String[] a = templet.deftype.split(";");
			String[] b = templet.defvalue.split(";");
			for( int i = 0; i < a.length; i++ ){
				defs.add( new Attackattr( Byte.parseByte( a[i] ), Float.parseFloat( b[i] )*count ) );
			}
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
	public int getHP() {
		return templet.hp*count;
	}
	public List<Attackattr> getAtks() {
		return atks;
	}
	public List<Attackattr> getDefs() {
		return defs;
	}
	public List<Integer> getAskings() {
		return askings;
	}
	public List<Integer> getAnswers() {
		return answers;
	}

	/**
	 * 包装攻击防御属性
	 * @param fighter
	 */
	public void wrapAttackattr( Fighter fighter ) {
		fighter.attacks.addAll( atks );
		fighter.defends.addAll( defs );
	}

}
