package cn.xgame.a.player.ectype.info;


import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.award.DropAward;
import cn.xgame.a.player.ectype.classes.IChapter;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ChapterPo;
import cn.xgame.config.o.EctypePo;

/**
 * 一个章节 - 该类数据不保存数据库
 * <br>注：endtime需要在外部设置
 * @author deng		
 * @date 2015-10-27 下午6:10:11
 */
public class ChapterInfo extends IChapter{

	// 深度时间
	private int depthtime;
	
	// 挂机副本列表
	private List<EctypeInfo> 	guajiEctypes = Lists.newArrayList();
	
	// 银宝箱
	private List<DropAward> 	silverpond = Lists.newArrayList();
	
	// 金宝箱
	private List<DropAward> 	goldenpond = Lists.newArrayList();
	
	// 奖励列表
	private List<DropAward> 	awards = Lists.newArrayList();
	
	
	public ChapterInfo( ChapterPo templet,int sid ) {
		super( templet.id, sid, templet.temp );
		depthtime = templet.stime;
		initGuajiEctype( templet.sleeptime );
		initPond( silverpond, templet.silverpond );
		initPond( goldenpond, templet.goldenpond );
		initAwards( templet.reward );
	}
	// 初始宝箱
	private void initPond( List<DropAward> list, String pond ) {
		if( pond.isEmpty() )
			return;
		String[] str = pond.split( "\\|" );
		for( String x : str ){
			list.add( new DropAward(x.split(";")) );
		}
	}
	// 初始挂机副本
	private void initGuajiEctype( String sleeptime ) {
		if( sleeptime.isEmpty() )
			return ;
		String[] array = sleeptime.split( ";" );
		for( int i = 0; i < array.length; i++ ){
			EctypePo templet = CsvGen.getEctypePo( getTempId() );
			if( templet == null ) 
				continue;
			EctypeInfo ectype = new EctypeInfo( (byte) (i+1), templet );
			// 挂机副本难度全部为1
			ectype.setAttribute( 1, templet );
			// 挂机副本 强制设置时间
			ectype.setFighttime( Integer.parseInt( array[i] ) );
			
			guajiEctypes.add(ectype);
		}
	}
	public void initAwards( String temp ){
		if( temp.isEmpty() ) return;
		String[] str = temp.split( "\\|" );
		for( String x : str ){
			awards.add( new DropAward( x.split(";") ) );
		}
	}
	
	public int getDepthtime() {
		return depthtime;
	}
	public List<EctypeInfo> getGuajiEctypes() {
		return guajiEctypes;
	}
	public List<DropAward> getSilverpond() {
		return silverpond;
	}
	public List<DropAward> getGoldenpond() {
		return goldenpond;
	}
	public List<DropAward> getAwards() {
		return awards;
	}

}
