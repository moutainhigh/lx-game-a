package cn.xgame.net.event.all.pl.staratlas;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;

import cn.xgame.a.player.fleet.o.FleetInfo;
import cn.xgame.a.player.fleet.other.IStatus;
import cn.xgame.a.player.fleet.other.StatusType;
import cn.xgame.a.player.fleet.purpose.Setsail;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.LuaUtil;

/**
 * 出航
 * @author deng		
 * @date 2015-10-9 下午3:43:21
 */
public class SailoutEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte fid	= data.readByte();//舰队ID
		List<Integer> airline = Lists.newArrayList();// 航线
		byte size	= data.readByte();
		for( int i = 0; i < size; i++ )
			airline.add( data.readInt() );
		
		ErrorCode code 	= null;
		IStatus status 	= null;
		try {
			
			// 获取舰队
			FleetInfo fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.getShips().isEmpty() || airline.isEmpty() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			status = fleet.getStatus();
			// 判断如果不在悬停状态 不能出航
			if( status.type() != StatusType.HOVER )
				throw new Exception( ErrorCode.FLEET_BUSY.name() );
			
			// 取出航线第一个目标星球
			int aimId = airline.remove(0);
			// 算出航行时间
			Lua lua = LuaUtil.getEctypeCombat();
			LuaValue[] value = lua.getField( "getSailingTime" ).call( 1, CsvGen.getStarsPo( fleet.getBerthSnid() ), CsvGen.getStarsPo( aimId ) );
			int sailtime = value[0].getInt();
			
			// 切换航行状态
			status = fleet.changeSail( aimId, sailtime, new Setsail( airline ) );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}

		ByteBuf respon = buildEmptyPackage( player.getCtx(), 125 );
		respon.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			respon.writeByte(fid);
			status.buildTransformStream( respon );
		}
		sendPackage( player.getCtx(), respon );
	}

}
