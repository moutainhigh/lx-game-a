package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.a.world.planet.home.o.OustChild;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.event.all.pl.update.Update_2252;
import cn.xgame.utils.Logs;
import cn.xgame.net.netty.Netty.RW;

/**
 * 发起驱逐元老
 * @author deng		
 * @date 2015-7-1 上午10:30:26
 */
public class SponsorExpelEvent extends IEvent{

	@Override
	public void run( Player player, ByteBuf data ) throws IOException {
		
		String uid 		= RW.readString(data);
		String explain 	= RW.readString(data);
		
		ErrorCode code = null;
		try {
			
			// 获取玩家 母星 - 这里暂时 默认在母星发起投票
			HomePlanet planet = WorldManager.o.getHPlanetInPlayer(player);
			
			// 判断 有没有权限
			Child child = planet.getChild( player.getUID() );
			if( child == null || !child.isSenator() )
				throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );
			// 判断 被驱逐的是不是 元老
			child 		= planet.getChild( uid );
			if( child == null || !child.isSenator() )
				throw new Exception( ErrorCode.NOT_SENATOR.name() );
			// 在看是不是已经在投票列表中
			OustChild oust = planet.getOustChild( uid );
			if( oust != null )
				throw new Exception( ErrorCode.YET_ATLIST.name() );
			
			oust = new OustChild( player, uid, explain );
			planet.getOustChild().add(oust);
			
			// 同步给其他玩家
			synchronizeGenrVote( planet.getPeoples(), 1, oust, child );
			
			Logs.debug( player.getCtx(), " 发起驱逐元老 uid=" + uid + ", explain=" + explain );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
		
	}
	
	private void synchronizeGenrVote( List<Child> childs, int isAdd, OustChild oust, Child x ) {
		for( Child child : childs ){
			Player player = PlayerManager.o.getPlayerFmOnline( child.getUID() );
			if( player == null || !player.isOnline() ) continue;
			((Update_2252)Events.UPDATE_2252.toInstance()).run( player, isAdd, oust, x );
		}
	}

}
