package cn.xgame.a.player.ectype.info;


import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.award.AwardInfo;
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
	
	// 奖励列表
	private List<DropAward> 	awards = Lists.newArrayList();
	
	// 难度列表
	private List<Byte>			difficultys = Lists.newArrayList();
	
	
	public ChapterInfo( ChapterPo templet,int sid ) {
		super( templet.id, sid, templet.temp );
		depthtime = templet.stime;
		initGuajiEctype( templet.sleeptime );
		initAwards( templet.reward );
		initDifficultys( templet.nquality );
	}
	// 初始难度列表
	private void initDifficultys( String nquality ) {
		String[] content = nquality.split( ";" );
		for( String str : content )
			difficultys.add( Byte.parseByte( str ) );
		if( difficultys.isEmpty() )
			difficultys.add( (byte) 1 );
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
			EctypeInfo ectype = new EctypeInfo( (byte) (i+1) );
			// 挂机副本难度全部为1
			ectype.setAttribute( 1, templet );
			// 挂机副本 强制设置时间
			ectype.setFighttime( Integer.parseInt( array[i] ) );
			
			guajiEctypes.add(ectype);
		}
	}
	// 初始奖励信息
	private void initAwards( String temp ){
		if( temp.isEmpty() ) return;
		String[] str = temp.split( "\\|" );
		for( String x : str ){
			awards.add( new DropAward( x.split(";") ) );
		}
	}
	
	/**
	 * 生成下一个难度的副本 
	 */
	public void generateNextEctype() {
		EctypePo templet = CsvGen.getEctypePo( getTempId() );
		if( templet == null ) 
			return;
		nextLevelEctype( templet );
	}
	/**
	 * 生成全部难度副本
	 */
	public void generateAllEctype() {
		EctypePo templet = CsvGen.getEctypePo( getTempId() );
		if( templet == null ) 
			return;
		for( int i = 0; i < difficultys.size(); i++ )
			nextLevelEctype( templet );
	}
	private void nextLevelEctype( EctypePo templet ) {
		int size = getEctypes().size();
		if( size >= difficultys.size() )
			return;
		byte level = difficultys.get(size);
		EctypeInfo ectype = new EctypeInfo( level );
		ectype.setAttribute( templet );
		getEctypes().add( ectype );
	}
	
	public EctypeInfo getGuajiEctype( byte level ) {
		for( EctypeInfo o : guajiEctypes ){
			if( o.getLevel() == level )
				return o;
		}
		return null;
	}
	
	/**
	 * 根据难度 获取副本信息
	 * @param ltype 1.普通本 2.挂机本
	 * @param level 难度
	 * @return
	 */
	public EctypeInfo getEctype(byte ltype, byte level) {
		if( ltype == 1 ){ // 普通本
			return getNormalEctype( level );
		}
		if( ltype == 2 ){ // 挂机本
			return getGuajiEctype( level );
		}
		return null;
	}
	
	/**
	 * 随机掉落奖励出来
	 * @param size 队员个数
	 * @param rateup 掉落几率加成
	 * @return
	 */
	public List<AwardInfo> randomAward( int size, int rateup ) {
		List<AwardInfo> ret = Lists.newArrayList();
		for( DropAward drop : awards ){
			if( drop.isDrop( rateup ) )
				ret.add( drop.toAward() );
		}
		return ret;
	}
	
	public int getDepthtime() {
		return depthtime;
	}
	public List<EctypeInfo> getGuajiEctypes() {
		return guajiEctypes;
	}
	public List<DropAward> getAwards() {
		return awards;
	}
	public List<Byte> getDifficultys() {
		return difficultys;
	}

}
