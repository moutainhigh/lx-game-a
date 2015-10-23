package cn.xgame.a.chat;


import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;
import java.util.List;

import cn.xgame.a.chat.axn.AxnControl;
import cn.xgame.a.chat.axn.classes.ChatType;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
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
public class ChatManager{
	private ChatManager(){}
	public static ChatManager o = new ChatManager();
	
	// 聊天频道操作
	private AxnControl chatControl = new AxnControl();
	public AxnControl getChatControl() {
		return chatControl;
	}
	
	
	/**
	 * 同步消息
	 * @param type
	 * @param axnId 
	 * @param sponsor 发起人
	 * @param content
	 */
	public void synchronizeMsg( ChatType type, int axnId, Player sponsor, String content ) {
		switch( type ){
		case WORLD:
			synWorldMsg( axnId, sponsor, content );
			break;
		case PLANET:
			synPlanetMsg( axnId, sponsor, content );
			break;
		default:
			synAxnMsg( axnId, sponsor, content );
			break;
		}
	}


	/**
	 * 同步世界消息
	 * @param axnId 
	 * @param sponsor 发起人
	 * @param content
	 */
	private void synWorldMsg( int axnId, Player sponsor, String content ) {
		
		Collection<Player> values = PlayerManager.o.getOnlinePlayer().values();
		for( Player o : values ){
			if( o.getUID().equals( sponsor.getUID() ) )
				continue;
			sendMsg( axnId, o.getCtx(), sponsor, content );
		}
	}


	/**
	 * 同步母星消息
	 * @param axnId 
	 * @param sponsor
	 * @param content
	 */
	private void synPlanetMsg( int axnId, Player sponsor, String content ) {
		
		// 获取玩家母星
		HomePlanet home;
		try {
			home = WorldManager.o.getHPlanetInPlayer(sponsor);
		} catch (Exception e) {
			return;
		}
		
		List<Child> ls = home.getPeoples();
		for( Child child : ls ){
			Player o = PlayerManager.o.getPlayerFmOnline( child.getUID() );
			if( o == null ) 
				continue;
			if( o.getUID().equals( sponsor.getUID() ) )
				continue;
			sendMsg( axnId, o.getCtx(), sponsor, content );
		}
	}
	
	/**
	 * 同步其他频道消息
	 * @param axn 
	 * @param sponsor
	 * @param content
	 */
	private void synAxnMsg( int axnId, Player sponsor, String content ) {
		
		AxnInfo axnInfo 		= chatControl.getAXNInfo( axnId );
		List<IAxnCrew> peoples 	= axnInfo.getAxnCrews();
		for( IAxnCrew people : peoples ){
			
			sendMsg( axnId, people.getSocket(), sponsor, content );
		}
	}
	
	/**
	 * 发送消息
	 * @param axnId
	 * @param socket
	 * @param sponsor
	 * @param content
	 */
	private void sendMsg( int axnId, ChannelHandlerContext socket, Player sponsor, String content) {
		if( content.isEmpty() || socket == null || !socket.channel().isActive() )
			return;
		((Update_3000)Events.UPDATE_3000.toInstance()).run( axnId, socket, sponsor, content );
	}



	
}
