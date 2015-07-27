package cn.xgame.a.chat;


import java.util.List;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.pl.update.Update_3000;


/**
 * 聊天管理中心
 * @author deng		
 * @date 2015-7-6 下午2:57:40
 */
public class ChatManager {

	private ChatManager(){}
	public static ChatManager o = new ChatManager();
	

	/**
	 * 同步消息
	 * @param type
	 * @param sponsor 发起人
	 * @param content
	 */
	public void synchronizeMsg( ChatType type, Player sponsor, String content ) {
		switch( type ){
		case WORLD:
//			synWorldMsg( sponsor, content );
			break;
		case PLANET:
			synPlanetMsg( sponsor, content );
			break;
		case TEAM:
			synTeamMsg( sponsor, content );
			break;
		case AXN_1:
			synAxnMsg( 0, sponsor, content );
			break;
		case AXN_2:
			synAxnMsg( 1, sponsor, content );
			break;
		case AXN_3:
			synAxnMsg( 2, sponsor, content );
			break;
		}
	}


	/**
	 * 同步世界消息
	 * @param sponsor 发起人
	 * @param content
	 */
//	private void synWorldMsg( Player sponsor, String content ) {
//		
//		Collection<Player> values = PlayerManager.o.getOnlinePlayer().values();
//		for( Player o : values ){
//			
//			synMsg( ChatType.WORLD, o, sponsor, content );
//		}
//	}

	/**
	 * 同步母星消息
	 * @param sponsor
	 * @param content
	 */
	private void synPlanetMsg( Player sponsor, String content ) {
		
		// 获取玩家母星
		HomePlanet home = WorldManager.o.getHPlanetInPlayer(sponsor);
		List<Child> ls = home.getPeoples();
		for( Child child : ls ){
			Player o = PlayerManager.o.getPlayerFmOnline( child.getUID() );
			if( o == null ) continue;
			
			synMsg( ChatType.PLANET, o, sponsor, content );
		}
	}
	
	/**
	 * 同步队伍消息
	 * @param sponsor
	 * @param content
	 */
	private void synTeamMsg( Player sponsor, String content ) {
		
	}
	
	/**
	 * 同步频道消息
	 * @param axnNo 频道号
	 * @param sponsor
	 * @param content
	 */
	private void synAxnMsg( int axnNo, Player sponsor, String content ) {
		
	}
	
	/**
	 * 同步私聊
	 * @param sponsor
	 * @param to
	 * @param content
	 */
	public void synPrivateMsg( Player sponsor, Player to, String content ) {
		synMsg( ChatType.PRIVATE, to, sponsor, content );
	}
	
	
	
	
	
	private void synMsg( ChatType type, Player to, Player sponsor, String content ) {
		if( !to.isOnline() )
			return;
		((Update_3000)Events.UPDATE_3000.getEventInstance()).run( ChatType.WORLD, to, sponsor, content );
	}


	
	
}
