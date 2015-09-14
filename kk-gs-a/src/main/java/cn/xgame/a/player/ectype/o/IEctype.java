package cn.xgame.a.player.ectype.o;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.Util.Random;
import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.award.DropAward;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.EctypePo;
import cn.xgame.system.LXConstants;

/**
 * 一个副本信息
 * @author deng		
 * @date 2015-9-10 下午4:25:58
 */
public class IEctype{
	
	private final EctypePo templet;
	
	// 奖励列表
	private List<DropAward> 	drops 	= Lists.newArrayList();
	
	// 怪物列表
	private List<Enemy> 		enemys	= Lists.newArrayList();
	
	
	public IEctype( int id ){
		this.templet	= CsvGen.getEctypePo(id);
		initDropAward();
		initEnemy();
	}
	private void initDropAward() {
		if( templet.reward.isEmpty() )
			return;
		String[] content = templet.reward.split("\\|");
		for( String str : content ){
			String[] x 		= str.split(";");
			DropAward drop 	= new DropAward( Integer.parseInt( x[0] ), Integer.parseInt( x[1] ), Integer.parseInt( x[2] ) );
			drops.add(drop);
		}
	}
	private void initEnemy() {
		if( templet.meetid.isEmpty() )
			return;
		String[] content = templet.meetid.split("\\|");
		for( String str : content ){
			String[] x = str.split( ";" );
			Enemy enemy = new Enemy( Integer.parseInt( x[0] ), Integer.parseInt( x[1] ) );
			enemys.add( enemy );
		}
	}
	
	public String toString(){
		return templet.id+" - drops=" + drops.size() + ", enemys=" + enemys.size();
	}
	
	public EctypePo templet(){ return templet; }
	
	
	public int getNid() {
		return templet.id;
	}
	
	/**
	 * 获取奖励的金币
	 * @return
	 */
	private int getRewardMoney(){
		if( templet.money.isEmpty() )
			return 0;
		String[] str = templet.money.split(";");
		return Random.get( Integer.parseInt( str[0] ), Integer.parseInt( str[1] ) );
	}
	
	/**
	 * 随机奖励出来
	 * @return
	 */
	public List<AwardInfo> randomAward() {
		List<AwardInfo> ret = Lists.newArrayList();
		ret.add( new AwardInfo( LXConstants.CURRENCY_NID, getRewardMoney() ) );
		for( DropAward drop : drops ){
			int rand = Random.get( 0, 10000 );
			if( rand > drop.getRand() ) continue;
			ret.add( new AwardInfo( drop.getId(), drop.getCount() ) );
		}
		return ret;
	}
	
	/**
	 * 返回一个战斗者
	 * @return
	 */
	public Fighter fighter(){
		Fighter fighter = new Fighter();
		for( Enemy enemy : enemys ){
			fighter.hp += enemy.getHP();
			enemy.wrapAttackattr( fighter );
		}
		return fighter;
	}

}
