package cn.xgame.net.event.all.pl.staratlas;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.fleet.info.purpose.Setsail;
import cn.xgame.a.player.fleet.info.status.SailStatus;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.LuaUtil;

/**
 * 申请航行结束
 * @author deng		
 * @date 2015-10-29 下午10:24:57
 */
public class ApplySailendEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte fid = data.readByte();//舰队ID
		
		ErrorCode code = null;
		FleetInfo fleet = null;
		try {
			// 获取舰队
			fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.getShips().isEmpty() )
				throw new Exception( ErrorCode.FLEET_BUSY.name() );
			if( !fleet.isSail() )
				throw new Exception( ErrorCode.FELLT_NOTSAIL.name() );
			SailStatus x = (SailStatus) fleet.getStatus();
			if( x.getPurpose().type() != 2 )
				throw new Exception( ErrorCode.FELLT_NOTSAIL.name() );
			if( !x.isComplete() )
				throw new Exception( ErrorCode.SUCCEED.name() );
			
			// 先设置当前停靠星球ID
			fleet.setBerthSnid( x.getAimId() );
			// 获取航站列表
			Setsail a = (Setsail) x.getPurpose();
			List<Integer> airline = a.getAirline();
			if( airline.isEmpty() ){// 没有航站了 那就悬停
			
				fleet.changeStatus( StatusType.HOVER );
			
			}else{ // 这里继续航行
				
				// 取出航线第一个目标星球
				int aimId = airline.remove(0);
				// 算出航行时间
				int sailtime = LuaUtil.getEctypeCombat().getField( "getSailingTime" ).call( 1, fleet.getBerthSnid(), aimId )[0].getInt();
				
				// 切换航行状态
				int starttime = (int) (System.currentTimeMillis()/1000);
				int endtime = starttime + sailtime;
				fleet.changeStatus( StatusType.SAIL, aimId, starttime, endtime, new Setsail( airline ) );
			}
				
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( fleet.getBerthSnid() );
			buffer.writeByte( fid );
			fleet.getStatus().buildTransformStream( buffer );
		}
		sendPackage( player.getCtx(), buffer );
	}

}
