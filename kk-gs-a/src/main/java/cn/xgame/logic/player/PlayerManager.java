package cn.xgame.logic.player;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import cn.xgame.net.event.Events;

public class PlayerManager {
	
	public static PlayerManager o = new PlayerManager();
	private PlayerManager(){}
	
	
	private Map<String, Player> players = new HashMap<String, Player>();
	
	
	public Player get( String uID ){
		return players.get(uID);
	}
	
	/**
	 * 获取 玩家  没有会在数据库获取
	 * @param uID
	 * @return
	 */
	public Player getPlayer( String uID ) {
		
		
		return null;
	}
	
	
	
	
	/**
	 * 针对玩家 的 消息分发
	 * @param uID
	 * @param event
	 * @param bufferData
	 * @throws Exception 
	 */
	public void eventRun( String uID, Events event, ByteBuf data ) throws Exception {
		
		Player player = getPlayer( uID );
		if( player == null )
			throw new Exception( "UID无效,UID="+uID );
		
		if( player.safeCheck( event ) )
			throw new Exception( "出现恶意刷包,UID="+uID );
		
		// 分发给对应 包处理
		event.run( player, data );
	}
	
	
	/**
	 * 有玩家 退出
	 * @param uID
	 */
	public void exit( String uID ) {
		// TODO Auto-generated method stub
		
	}

	public int peopleNumber() {
		return players.size();
	}
	
	
	

}
