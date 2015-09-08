package cn.xgame.a.player.ectype;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.Util.Random;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.combat.CombatUtil;
import cn.xgame.a.combat.o.Answers;
import cn.xgame.a.combat.o.Askings;
import cn.xgame.a.combat.o.AtkAndDef;
import cn.xgame.a.player.ectype.enemy.DropAward;
import cn.xgame.a.player.ectype.enemy.Enemy;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.AskingPo;
import cn.xgame.config.o.EctypePo;
import cn.xgame.system.LXConstants;

/**
 * 副本基类
 * @author deng		
 * @date 2015-7-25 下午12:57:43
 */
public abstract class IEctype {
	
	protected final EctypePo templet;
	
	// 所属星球ID
	public final int SNID ;
	
	// 副本类型
	protected EctypeType type;
	
	// 基础应答 - 问
	protected List<AskingPo> basisAskings 	= Lists.newArrayList();
	// 随机应答 - 问
	protected List<AskingPo> randomAskings 	= Lists.newArrayList();
	
	// 敌人列表
	protected List<Enemy> enemys 			= Lists.newArrayList();
	
	// 奖励列表
	private List<DropAward> drops 			= Lists.newArrayList();
	
	/**
	 * 从配置表获取
	 * @param id 所属星球ID
	 * @param src
	 */
	public IEctype( int id, EctypePo src ) {
		SNID 		= id;
		templet 	= src;
		type		= EctypeType.fromNumber( templet.type );
		randomAsking();
		initBasisAsking();
		initDropAward();
		initEnemy();
	}


	/**
	 * 从数据库 获取
	 * @param buffer
	 */
	public IEctype( ByteBuf buffer ){
		SNID 		= buffer.readInt();
		templet 	= CsvGen.getEctypePo( buffer.readInt() );
		type		= EctypeType.fromNumber( templet.type );
		byte size 	= buffer.readByte();
		for( int i = 0; i < size; i++ ){
			AskingPo o = CsvGen.getAskingPo( buffer.readInt() );
			randomAskings.add(o);
		}
		initBasisAsking();
		initDropAward();
		initEnemy();
	}

	/**
	 * 返回到数据库
	 * @param buffer
	 */
	public void putBuffer(ByteBuf buffer) {
		buffer.writeInt( SNID );
		buffer.writeInt( templet.id );
		buffer.writeByte( randomAskings.size() );
		for( AskingPo o : randomAskings ){
			buffer.writeInt( o.id );
		}
	}

	/** 随机应答出来 */
	public void randomAsking() {
		randomAsking( templet.ranswers );
		randomAsking( templet.ianswers );
	}

	// 初始副本基础应答
	private void initBasisAsking() {
		if( templet.eanswers.isEmpty() )
			return;
		String[] content = templet.eanswers.split(";");
		for( String v : content ){
			AskingPo o = CsvGen.getAskingPo( Integer.parseInt( v ) );
			basisAskings.add( o );
		}
	}
	private void randomAsking( String answers ) {
		if( answers.isEmpty() )
			return;
		String[] content = answers.split("\\|");
		for( String x : content ){
			if( x.isEmpty() ) continue;
			String[] v = x.split(";");
			int rand = Random.get( 0, 10000 );
			if( rand > Integer.parseInt( v[1] ) ) 
				continue;
			AskingPo o = CsvGen.getAskingPo( Integer.parseInt( v[0] ) );
			randomAskings.add( o );
		}
	}
	
	// 初始化奖励
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
		
	// 初始化敌人
	private void initEnemy() {
		if( templet.meetid.isEmpty() )
			return;
		String[] content = templet.meetid.split("\\|");
		for( String str : content ){
			if( str.isEmpty() ) continue;
			String[] x 	= str.split(";");
			Enemy enemy = new Enemy( Integer.parseInt( x[0] ) );
			enemy.setCount( Integer.parseInt( x[1] ) );
			enemys.add(enemy);
		}
	}
	
	/**
	 * 刷新奖励
	 * @return
	 */
	public List<AwardInfo> updateAward() {
		List<AwardInfo> ret = Lists.newArrayList();
		// 先计算副本身奖励
		for( DropAward drop : drops ){
			if( !drop.isDrop() )
				continue;
			ret.add(drop);
		}
		// 金币
		if( !templet.money.isEmpty() ){
			String[] str = templet.money.split( ";" );
			int retmoney = Random.get( Integer.parseInt( str[0] ), Integer.parseInt( str[1] ) );
			if( retmoney != 0 )
				ret.add( new AwardInfo( LXConstants.CURRENCY_NID, retmoney ) );
		}
		// 在计算怪物身上的
		for( Enemy enemy : enemys ){
			List<DropAward> ls = enemy.getDrops();
			for( DropAward drop : ls ){
				if( !drop.isDrop() )
					continue;
				ret.add(drop);
			}
		}
		return ret;
	}
	
	
	/**
	 * 该副本是否关闭
	 * @return
	 */
	public abstract boolean isClose();
	public EctypePo templet(){ return templet; }
	public int getNid() { return templet.id; }
	public EctypeType type(){ return type ; }
	public List<DropAward> getDrops() {
		return drops;
	}

	///////////////----------------------战斗相关
	/**
	 * 包装战斗 问
	 * @param askings
	 */
	public void wrapAsking(List<Askings> askings) {
		List<AskingPo> all = Lists.newArrayList();
		all.addAll( basisAskings );
		all.addAll( randomAskings );
		for( AskingPo ask : all ){
			Askings o = new Askings(ask);
			askings.add(o);
		}
	}
	
	/**
	 * 包装怪物数据
	 * @param attacks
	 * @param defends
	 * @param askings
	 * @param answers
	 */
	public int wrapEnemy(List<AtkAndDef> attacks, List<AtkAndDef> defends,
			List<Askings> askings, List<Answers> answers) {
		
		int hp = 0;
		for( Enemy enemy : enemys ){
			// 血量
			hp += enemy.getHP();
			// 基础属性
			CombatUtil.putBasisProperty( attacks, enemy.getAtkType(), enemy.getAtkValue() );
			CombatUtil.putBasisProperty( defends, enemy.getDefType(), enemy.getDefValue() );
			// 问
			CombatUtil.putAsking( enemy.templet().askings, askings );
			// 答
		}
		return hp;
	}

	
}
