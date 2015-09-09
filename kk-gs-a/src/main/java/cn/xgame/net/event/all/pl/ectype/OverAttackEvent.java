package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.captain.o.CaptainInfo;
import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.ship.o.status.ShipStatus;
import cn.xgame.a.player.ship.o.status.StatusControl;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;

/**
 * 申请结束副本
 * @author deng		
 * @date 2015-7-31 上午7:43:03
 */
public class OverAttackEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int suid = data.readInt();
		
		Logs.debug( player, "申请结束副本 , 舰船UID=" + suid );
		
		ErrorCode code 			= null;
		int combatTime 			= 0;// 战斗时间
		byte iswin 				= 0;
		List<IProp> ret 		= null;
		List<AwardInfo> awards	= null;
		try {
			
			// 判断副本是否可以打
//			IEctype ectype = player.getEctypes().getEctype( snid, enid );
//			if( ectype == null )
//				throw new Exception( ErrorCode.ECTYPE_NOTEXIST.name() );
			
			ShipInfo ship 			= player.getDocks().getShip(suid);
			StatusControl status 	= ship.getStatus();
			if( status.getStatus() == ShipStatus.COMBAT ){
				combatTime			= status.getSurplusTime();
				if( combatTime != 0 )
					throw new Exception( ErrorCode.COMBATTIME_NOTOVER.name() );
			}else{
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			}
			
			// 这里设置 悬停状态
			status.levitation();
			
			// 获取奖励
			awards	= ship.getKeepInfo().getAwards();
			iswin	= ship.getKeepInfo().isWin();
			if( iswin == 1 )
				ret	= ship.getKeepInfo().giveoutAward( status.getCurrentSnid(), player );
			
			// 清空副本记录
			ship.getKeepInfo().clear();
			
			// 这里结算舰船的舰长忠诚度
			CaptainInfo captain = player.getCaptains().getCaptain( ship.getCaptainUID() );
			if( captain != null ){
				if( captain.changeLoyalty( iswin == 1 ? 1 : -1 ) ){// 这里如果没有忠诚度了 就要删除掉
					player.getCaptains().destroy( captain );
					ship.setCaptainUID( -1 );
				}
			}
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		response.writeInt( suid );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeByte( iswin );
			if( iswin == 1 ){
				// 奖励个数 用于显示
				response.writeByte( awards.size() );
				for( AwardInfo award : awards )
					award.buildTransformStream(response);
				// 奖励个数 用于更新
				response.writeByte( ret.size() );
				for( IProp prop : ret ){
					prop.putBaseBuffer(response);
				}
			}
		}
		// 时间还没结束 让前端继续播放
		if( code == ErrorCode.COMBATTIME_NOTOVER ){
			response.writeInt( combatTime );
		}
		sendPackage( player.getCtx(), response );
		
	}

}
