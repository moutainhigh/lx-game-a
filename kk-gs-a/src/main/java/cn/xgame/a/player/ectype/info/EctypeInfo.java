package cn.xgame.a.player.ectype.info;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.Util.Random;

import cn.xgame.a.award.DropAward;
import cn.xgame.a.player.ectype.classes.IEctype;
import cn.xgame.config.o.EctypePo;
import cn.xgame.utils.LuaUtil;

/**
 * 一个副本信息 - 该类数据不保存数据库
 * @author deng		
 * @date 2015-10-27 下午7:44:04
 */
public class EctypeInfo extends IEctype{

	// 奖励
	private List<DropAward> awards = Lists.newArrayList();
	
	// 金币奖励
	private int[] money = new int[2];
	
	// 战斗时间
	private int fighttime ;
	
	// 最大成功率
	private short maxSuccessRate;
	
	public EctypeInfo( byte level ) {
		super(level);
	}

	/**
	 * 通过lua脚本设置属性
	 * @param type 是否随机
	 * @param templet 
	 */
	public void setAttribute( String type, EctypePo templet ){
		LuaUtil.getEctypeInfo().getField( "generateEctypeInfo_" + type ).call( 0, this, getLevel(), templet );
	}
	
	public List<DropAward> getAwards() {
		return awards;
	}
	public void setAwards( String temp ){
		if( temp.isEmpty() ) return;
		String[] str = temp.split( "\\|" );
		for( String x : str ){
			awards.add( new DropAward( x.split(";") ) );
		}
	}
	public void setMoney( String temp ) {
		if( temp.isEmpty() ) return;
		String[] str = temp.split(";");
		this.money[0] = Integer.parseInt(str[0]);
		this.money[1] = Integer.parseInt(str[1]);
	}
	public int getMoney(){
		return Random.get( money[0], money[1] );
	}
	public int getFighttime() {
		return fighttime;
	}
	public void setFighttime( int fighttime ) {
		this.fighttime = fighttime;
	}
	public short getMaxSuccessRate() {
		return maxSuccessRate;
	}
	public void setMaxSuccessRate(short maxSuccessRate) {
		this.maxSuccessRate = maxSuccessRate;
	}
	
}
