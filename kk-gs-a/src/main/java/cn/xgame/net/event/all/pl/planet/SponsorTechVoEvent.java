package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.data.tech.TechControl;
import cn.xgame.a.world.planet.data.tech.UnTechs;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.a.world.planet.home.o.Syn;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;

/**
 * 发起科技投票
 * @author deng		
 * @date 2015-7-1 上午10:29:20
 */
public class SponsorTechVoEvent extends IEvent{

	private final int[] VoteTime = { 43200, 86400, 172800 };
	
	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	
		int nid 	= data.readInt();
		byte type 	= data.readByte();
		
		ErrorCode code = null;
		
		try {
			// 先将时间转换
			int time = VoteTime[type-1] + (int)(System.currentTimeMillis()/1000);
			
			// 获取玩家 母星 - 这里暂时 默认在母星发起投票
			HomePlanet planet = WorldManager.o.getHPlanetInPlayer(player);
			
			// 判断 有没有权限
			Child child = planet.getChild( player.getUID() );
			if( child == null || !child.isSenator() )
				throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );
			
			// 判断是否能研究
			TechControl techmanage = planet.getTech();
			if( !techmanage.isCanStudy( nid, planet.getTechLevel() ) )
				throw new Exception( ErrorCode.CON_DISSATISFY.name() );
			
			// 添加到投票列表
			UnTechs unTechs = techmanage.appendVote( player, nid, time );
			
			// 记录玩家发起数
			child.addSponsors( 1 );
			
			// 下面同步消息给玩家
			Syn.tech( planet.getPeoples(), 1, unTechs );
			
			Logs.debug( player.getCtx(), " 发起科技投票 nid=" + nid + ", time=" + time );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
		
	}

}
