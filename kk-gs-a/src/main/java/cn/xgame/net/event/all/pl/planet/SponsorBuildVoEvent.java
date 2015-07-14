package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.LuaUtil;

/**
 * 发起建筑投票 
 * @author deng		
 * @date 2015-7-1 上午10:27:47
 */
public class SponsorBuildVoEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		int nid 	= data.readInt();
		byte index 	= data.readByte();
		byte type 	= data.readByte();
		
		ErrorCode code = null;
		
		try {
			// 先将时间转换
			Lua lua = LuaUtil.getGameData();
			LuaValue[] ret = lua.getField( "getVoteTime" ).call( 1, type );
			int time = ret[0].getInt();
			
			// 获取玩家 母星 - 这里暂时 默认在母星发起投票
			IPlanet planet = WorldManager.o.getHPlanetInPlayer(player);
			
			// 开始发起投票
			planet.sponsorBuivote( player, nid, index, time );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
	}


}
