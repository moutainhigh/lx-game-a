package cn.xgame.net.event.all.pl.staratlas;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.fleet.info.purpose.Setsail;
import cn.xgame.a.player.fleet.info.status.SailStatus;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 修改航线
 * @author deng		
 * @date 2015-10-9 下午5:51:12
 */
public class ModifyAirlineEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte fid	= data.readByte();//舰队ID
		List<Integer> airline = Lists.newArrayList();// 航线
		byte size	= data.readByte();
		for( int i = 0; i < size; i++ )
			airline.add( data.readInt() );
		
		ErrorCode code 		= null;
		SailStatus status 	= null;
		try {
			
			// 获取舰队
			FleetInfo fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.getShips().isEmpty() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			status = (SailStatus)fleet.getStatus();
			// 判断如果不在航行状态 不能修改
			if( status.type() != StatusType.SAIL )
				throw new Exception( ErrorCode.FLEET_BUSY.name() );

			// 修改航线
			Setsail purpose = (Setsail) status.getPurpose();
			purpose.resetAirline(airline);
			
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
