package cn.xgame.a.player.u;

import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.PlayerInit;

/**
 * 玩家初始化
 * @author deng		
 * @date 2015-7-9 下午12:58:57
 */
public class Init {

	private static final PlayerInit o = CsvGen.playerinits.get(0);
	
	/**
	 * 玩家初始化
	 * @param uID 玩家唯一ID
	 * @param headIco 头像ID
	 * @param adjutantId 副官ID
	 * @param name 玩家名字
	 * @return
	 */
	public static Player run( String uID, int headIco, int adjutantId, String name ) {
		
		Player ret = new Player( uID, headIco, name );
		ret.setAdjutantId( adjutantId );
		
		ret.setCurrency( o.currency );
		ret.setGold( o.gold );
		
		// 添加 初始道具
		initItem(ret);
		
		// 添加 初始舰船
		initShip(ret);
		
		// 添加 初始舰长
		initCaptain( ret );
		
		return ret;
	}

	private static void initItem( Player ret ) {
		String[] items = o.item.split("\\|");
		if( items == null ) return ;
		for( String x : items ){
			if( x.isEmpty() ) continue;
			String[] v = x.split(";");
			ret.getProps().appendProp( Integer.parseInt( v[0] ), Integer.parseInt( v[1] ) );
		}
	}
	
	private static void initShip( Player ret ) {
		String[] ls = o.ship.split(";");
		for( String x : ls ){
			if( x.isEmpty() ) continue;
			ret.getDocks().createShip( Integer.parseInt( x ) );
		}
	}
	
	private static void initCaptain( Player ret ) {
		String[] ls = o.captain.split(";");
		for( String x : ls ){
			if( x.isEmpty() ) continue;
			ret.getCaptains().createCaptain( Integer.parseInt( x ) );
		}
	}
	
}
