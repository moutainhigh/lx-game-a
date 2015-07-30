package cn.xgame.a.player.ectype.enemy;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.EnemyPo;
import cn.xgame.config.o.WeaponPo;


/**
 * 一个怪物
 * @author deng		
 * @date 2015-7-30 下午10:31:50
 */
public class Enemy {

	private final EnemyPo templet;
	
	// 数量
	private int count = 1;
	
	// 等级
	private short lv = 1;

	// 装备列表
	private List<WeaponPo> equips = Lists.newArrayList();
	
	public Enemy(int id) {
		templet = CsvGen.getEnemyPo(id);
	}
	
	public EnemyPo templet(){ return templet; }

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public short getLv() {
		return lv;
	}
	public void setLv(short lv) {
		this.lv = lv;
	}
	public List<WeaponPo> getEquips() {
		return equips;
	}
	public void wrapEquips(String string) {
		if( string.isEmpty() ) return;
		String[] x = string.split(";");
		for( String v : x ){
			if( v.isEmpty() ) continue;
			WeaponPo o = CsvGen.getWeaponPo( Integer.parseInt(v) );
			equips.add(o);
		}
	}
	
	
}
