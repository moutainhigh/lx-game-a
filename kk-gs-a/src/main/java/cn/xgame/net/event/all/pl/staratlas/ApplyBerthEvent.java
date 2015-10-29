package cn.xgame.net.event.all.pl.staratlas;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.fleet.classes.IStatus;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.fleet.info.purpose.Backsail;
import cn.xgame.a.player.fleet.info.purpose.Setsail;
import cn.xgame.a.player.fleet.info.status.SailStatus;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请就近停靠
 * @author deng		
 * @date 2015-10-29 下午10:38:16
 */
public class ApplyBerthEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte fid = data.readByte();//舰队ID
		
		ErrorCode code = null;
		IStatus status = null;
		try {
			// 获取舰队
			FleetInfo fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.getShips().isEmpty() )
				throw new Exception( ErrorCode.FLEET_BUSY.name() );
			if( !fleet.isSail() )
				throw new Exception( ErrorCode.FELLT_NOTSAIL.name() );
			status = fleet.getStatus();
			SailStatus x = (SailStatus) status;
			if( x.getPurpose().type() != 2 )
				throw new Exception( ErrorCode.FELLT_NOTSAIL.name() );
			
			// 根据时间算出 是返回还是继续往前走
			int backtime = isBack( x );
			if( backtime != 0 ){
			
				int starttime = (int) (System.currentTimeMillis()/1000);
				int endtime = starttime + backtime;
				status = fleet.changeStatus( StatusType.SAIL, fleet.getBerthSnid(), starttime, endtime, new Backsail() );
			
			}else{ // 如果继续往前走 直接将航站列表清空即可
				
				((Setsail) x.getPurpose()).resetAirline(null);
			}
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( fid );
			status.buildTransformStream( buffer );
		}
		sendPackage( player.getCtx(), buffer );
	}
	
	/**
	 * 根据航行距离 确定那边更近 然后确定是否返航
	 * @param x
	 * @return 返航需要的时间
	 */
	private int isBack( SailStatus x ) {
		int curtime = (int) (System.currentTimeMillis()/1000);
		// 获取已经航行的时间
		curtime 	= curtime - x.getStarttime();
		curtime		= curtime < 0 ? 1 : curtime;
		// 一共需要航行的时间
		int sumtime = x.getEndtime() - x.getStarttime();
		// 这里看 如已经航行的时间 小于 总时间的一半 那么就说明是返航
		return curtime < sumtime/2 ? curtime : 0;
	}

}
