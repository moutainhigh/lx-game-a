package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;
import x.javaplus.util.Util.Random;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;

import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.ectype.combat.Fighter;
import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.ship.o.status.SailPurpose;
import cn.xgame.a.player.ship.o.status.ShipStatus;
import cn.xgame.a.player.ship.o.status.StatusControl;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

/**
 * 开始攻击
 * @author deng		
 * @date 2015-7-13 下午7:27:11
 */
public class StartAttackEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {

//		int snid = data.readInt();
//		int enid = data.readInt();
//		int suid = data.readInt();
//		
//		Logs.debug( player, "申请攻打副本 星球ID=" + snid + ", 副本ID=" + enid + ", 舰船UID=" + suid );
//		
//		ErrorCode code = null;
//		
//		int sailTime	= 0;// 航行时间
//		int combatTime 	= 0;// 战斗时间
//		byte isWin 		= 0;// 是否胜利
//		List<AwardInfo> awards = null;
//		try {
//			
//			// 判断副本是否可以打
//			IEctype ectype = player.getEctypes().getEctype( snid, enid );
//			if( ectype == null )
//				throw new Exception( ErrorCode.ECTYPE_NOTEXIST.name() );
//			
//			ShipInfo ship 			= player.getDocks().getShipOfException(suid);
//			// 检测舰船是否可以战斗
//			if( !ship.isCanFighting() )
//				throw new Exception( ErrorCode.OTHER_ERROR.name() );
//			StatusControl status 	= ship.getStatus();
//			
//			if( status.getStatus() == ShipStatus.SAILING && status.getSailPurpose() != SailPurpose.FIGHTING  )
//				throw new Exception( ErrorCode.OTHER_ERROR.name() );
//			if( status.getStatus() == ShipStatus.COMBAT )
//				throw new Exception( ErrorCode.OTHER_ERROR.name() );
//			
//			// ------开始结算结果
//			
//			// 这里计算组队信息
//			// TODO 
//			Fighter att = new Fighter( player, ship );// 攻击者
//			Fighter def = new Fighter( ectype );// 防御者
//			try {
//				Lua lua 		= LuaUtil.getEctypeCombat();
//				// 攻击者 防御者 基础战斗时间 胜率上限
//				LuaValue[] ret 	= lua.getField( "ectypeFight" ).call( 4, att, def, ectype.templet().btime, ectype.templet().maxran );
//				combatTime		= ret[0].getInt();
//				int winRate 	= ret[1].getInt();
//				int warDamaged 	= ret[2].getInt();
//				int ammoExpend	= ret[3].getInt();
//				
//				// 算出胜负
//				int rand 		= Random.get( 0, 10000 );
//				isWin 			= (byte) (rand <= winRate ? 1 : 0);
//				// 根据胜利 获取算出奖励
//				awards 			= (isWin == 1 ? ectype.updateAward() : null);
//				// 切换战斗状态
//				status.startAttack( combatTime );
//				// 这里记录战斗 信息
//				ship.recordEctypeCombatInfo( enid, isWin, awards );
//				
//				Logs.debug( player, "攻打副本结果 combatTime=" + combatTime + ", winRate=" + winRate + ", warDamaged=" + warDamaged + ", ammoExpend=" + ammoExpend );
//			} catch ( Exception e ) {
//				Logs.error( player, "副本战斗错误 ", e );
//			}
//			
//			// 获取航行时间  这里主要检查 是否已经到达该星球 
//			if( status.getStatus() == ShipStatus.LEVITATION ){
//				sailTime = ship.getSailingTime( snid );
//				if( sailTime != 0 ){
//					status.startSail( snid, sailTime, SailPurpose.FIGHTING );
//					throw new Exception( ErrorCode.SHIP_NOTINSTAR.name() );
//				}
//			}
//			
//			// 这里检测是否还需要航行 
//			if( status.getStatus() == ShipStatus.SAILING && status.getSailPurpose() == SailPurpose.FIGHTING ){
//				sailTime = status.getSurplusTime();
//				if( sailTime != 0 ) throw new Exception( ErrorCode.SHIP_NOTINSTAR.name() );
//			}
//			
//			// 航行完了 就悬停
//			if( sailTime == 0 && status.getStatus() == ShipStatus.SAILING ){
//				status.levitation();
//			}
//			
//			code = ErrorCode.SUCCEED;
//		} catch (Exception e) {
//			code = ErrorCode.valueOf( e.getMessage() );
//		}
//		
//		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
//		response.writeInt( suid );
//		response.writeShort( code.toNumber() );
//		if( code == ErrorCode.SUCCEED ){
//			response.writeInt( combatTime );
//			Logs.debug(player, "申请出击副本 出击成功 combatTime=" + combatTime + ", isWin=" + isWin + ", awards=" + awards );
//		}
//		// 该船不在目标星球  这里叫他航行
//		if( code == ErrorCode.SHIP_NOTINSTAR ){
//			response.writeInt( snid );
//			response.writeInt( sailTime );
//			Logs.debug(player, "申请出击副本 不在目标星球 sailTime=" + sailTime );
//		}
//		// 等待其他玩家
//		if( code == ErrorCode.AWAIT_OTHERPLAYER ){
//			response.writeInt( 30 );
//		}
//		sendPackage( player.getCtx(), response );
	}

	
}
