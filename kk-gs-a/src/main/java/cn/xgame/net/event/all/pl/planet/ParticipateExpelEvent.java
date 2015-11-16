package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.data.vote.VotePlayer;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.a.world.planet.home.o.OustChild;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.event.all.pl.update.Update_2252;
import cn.xgame.utils.Logs;
import cn.xgame.net.netty.Netty.RW;

/**
 * 参与驱逐元老投票
 * @author deng		
 * @date 2015-7-2 上午11:04:04
 */
public class ParticipateExpelEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		String uid 		= RW.readString(data);
		byte isAgree 	= data.readByte();
		
		ErrorCode code = null;
		
		try {
			// 获取对应星球  - 这里暂时 默认在母星发起投票
			HomePlanet planet = WorldManager.o.getHPlanetInPlayer(player);
			
			// 判断是否有权限投票  只有元老才能投票
			Child child 	= planet.getChild( player.getUID() );
			if( child == null || child.isSenator() )
				throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );
			
			// 判断是否已经参与投票了
			OustChild oust 	= planet.getOustChild( uid );
			if( oust == null )
				throw new Exception( ErrorCode.VOTE_NOTEXIST.name() );
			
			// 这里先将玩家已经投过的票清除
			oust.getVote().purgeVote( child );
			
			// 设置投票
			byte status = oust.getVote().setIsAgrees( new VotePlayer( child ), isAgree );
			// 说明投票完成
			if( status != -1 ){
				child.setExpel( status == 1 ? true : false );
				// 最后不管怎样都要删掉的
				planet.removeVoteGenr( oust );
				// 同步
				synchronizeGenrVote( planet.getPeoples(), 0, oust, planet.getChild( uid ) );
			}
			
			Logs.debug( player.getCtx(), "参与元老投票 当前票数 " + oust.getVote() + " at=" + uid );
			
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
