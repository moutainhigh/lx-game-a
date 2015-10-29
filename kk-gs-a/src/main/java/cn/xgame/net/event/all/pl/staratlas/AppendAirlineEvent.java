package cn.xgame.net.event.all.pl.staratlas;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.fleet.info.purpose.Setsail;
import cn.xgame.a.player.fleet.info.status.SailStatus;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 追加航线
 * @author deng		
 * @date 2015-10-9 下午5:51:12
 */
public class AppendAirlineEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte fid	= data.readByte();//舰队ID
		List<Integer> airline = Lists.newArrayList();// 航线
		byte size	= data.readByte();
		for( int i = 0; i < size; i++ )
			airline.add( data.readInt() );
		
		ErrorCode code 		= null;
		try {
			// 获取舰队
			FleetInfo fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.getShips().isEmpty() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			if( !fleet.isSail() )
				throw new Exception( ErrorCode.FELLT_NOTSAIL.name() );
			SailStatus x = (SailStatus) fleet.getStatus();
			Setsail purpose = (Setsail) x.getPurpose();
			if( purpose.type() != 2 )
				throw new Exception( ErrorCode.FELLT_NOTSAIL.name() );
			
			// 追加航线
			purpose.appendAirline( airline );
			
			// 这里加入返回前端消息
			airline.clear();
			airline.addAll( purpose.getAirline() );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}

		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( airline.size() );
			for( int id : airline )
				buffer.writeInt(id);
		}
		sendPackage( player.getCtx(), buffer );
	}

}
