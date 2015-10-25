package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.data.building.BuildingControl;
import cn.xgame.a.world.planet.data.building.UnBuildings;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.a.world.planet.home.o.Syn;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.SbuildingPo;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;

/**
 * 发起建筑投票 
 * @author deng		
 * @date 2015-7-1 上午10:27:47
 */
public class SponsorBuildVoEvent extends IEvent{

	private final int[] VoteTime = { 43200, 86400, 172800 };
	
	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		int nid 	= data.readInt();
		byte index 	= data.readByte();
		byte type 	= data.readByte();
		
		ErrorCode code = null;
		
		try {
			// 先将时间转换
			int time = VoteTime[type-1] + (int)(System.currentTimeMillis()/1000);
			
			// 获取玩家 母星 - 这里暂时 默认在母星发起投票
			HomePlanet planet = WorldManager.o.getHPlanetInPlayer(player);
			
			// 判断是否有权限发起投票
			Child child = planet.getChild( player.getUID() );
			if( child == null || !child.isSenator() )
				throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );
			
			// 判断位置是否占用
			BuildingControl buildingControl = planet.getBuildingControl();
			SbuildingPo templet = CsvGen.getSbuildingPo(nid);
			if( buildingControl.isOccupyInIndex( index, templet.usegrid ) )
				throw new Exception( ErrorCode.INDEX_OCCUPY.name() );
			
			// 判断该建筑能不能建
			if( !buildingControl.isCanBuild( nid ) )
				throw new Exception( ErrorCode.YET_ATLIST.name() );
			
			// 添加到投票中
			UnBuildings voteBuild = buildingControl.appendVoteBuild( player, nid, index, time );
			
			// 记录玩家发起数
			child.addSponsors( 1 );
			
			// 下面同步消息给玩家
			Syn.build( planet.getPeoples(), 1, voteBuild );
			
			Logs.debug( player.getCtx(), " 发起建筑投票 nid=" + nid + ", index=" + index + ", time=" + time );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
	}


}
