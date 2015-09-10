package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.Util.Time;
import x.javaplus.util.lua.Lua;


import cn.xgame.a.ectype.ChapterEctype;
import cn.xgame.a.ectype.IEctype;
import cn.xgame.a.player.ectype.EctypeControl;
import cn.xgame.a.player.ship.o.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.LuaUtil;

/**
 * 申请某个星球的副本信息
 * @author deng		
 * @date 2015-7-20 上午10:12:10
 */
public class ApplyEctypeEvent extends IEvent{
	
	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte fleetId	= data.readByte();
		int snid 		= data.readInt();
		
		IPlanet planet = WorldManager.o.getPlanet(snid);
		if( planet == null ) return;
		
		FleetInfo fleet 		= player.getDocks().getFleetInfo( fleetId );
		EctypeControl control 	= player.getEctypes();
		
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		
		// 获取副本列表
		List<List<ChapterEctype>> chapters = control.getEctypeList(planet);
		
		// 常规副本
		List<ChapterEctype> general = chapters.get(0);
		response.writeByte( general.size() );
		for( ChapterEctype o : general ){
			o.buildTransformStream(response);
			response.writeInt( (int) (Time.toWeehoursTime()/1000) );
			List<IEctype> ectypes = o.getEctypes();
			response.writeByte( ectypes.size() );
			for( IEctype x : ectypes ){
				response.writeInt( x.getNid() );
				Lua lua = LuaUtil.getEctypeCombat();
				lua.getField( "arithmeticShowData" ).call( 0, x, fleet, response );
			}
		}
		// 普通限时副本
		List<ChapterEctype> normals = chapters.get(1);
		response.writeByte( normals.size() );
		for( ChapterEctype o : normals ){
			o.buildTransformStream(response);
			response.writeInt( o.getSurplusTime() );
			List<IEctype> ectypes = o.getEctypes();
			response.writeByte( ectypes.size() );
			for( IEctype x : ectypes ){
				response.writeInt( x.getNid() );
				Lua lua = LuaUtil.getEctypeCombat();
				lua.getField( "arithmeticShowData" ).call( 0, x, fleet, response );
			}
		}
		// 特殊限时副本
		response.writeByte( 0 );
		sendPackage( player.getCtx(), response );
	}

	
	
}
