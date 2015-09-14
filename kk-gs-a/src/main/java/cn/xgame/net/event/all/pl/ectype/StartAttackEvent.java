package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;
import x.javaplus.util.Util.Random;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;


import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.dock.capt.CaptainInfo;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.ectype.o.IEctype;
import cn.xgame.a.player.fleet.o.FleetInfo;
import cn.xgame.a.player.fleet.other.IStatus;
import cn.xgame.a.player.fleet.other.StatusType;
import cn.xgame.a.player.fleet.purpose.FightEctype;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
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

		int snid 	= data.readInt();// 星球ID
		byte type	= data.readByte(); // 类型
		int cnid 	= data.readInt();// 章节ID
		int enid 	= data.readInt();// 副本ID
		int fid 	= data.readByte();// 舰队ID
		
		ErrorCode code 	= null;
		IStatus status 	= null;
		try {
			
			Logs.debug( player, "申请攻打副本 星球ID=" + snid + "类型=" + type + ", 章节ID=" + cnid + ", 副本ID=" + enid + ", 舰队ID=" + fid );
			
			// 获取舰队
			FleetInfo fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.getShips().isEmpty() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			status 			= fleet.getStatus();
			
			// 获取副本
			IEctype ectype = player.getEctypes().getEctype( snid, type, cnid, enid );
			if( ectype == null )
				throw new Exception( ErrorCode.ECTYPE_NOTEXIST.name() );
			
			// 如果是在航行状态下 并且还没航行完的话 直接让他继续航行
			if( status.type() == StatusType.SAIL && !status.isComplete() )
				throw new Exception( ErrorCode.SUCCEED.name() );
			
			// 没有在同一个星球的话 就让他航行
			if( fleet.getBerthSnid() != snid ){
				Lua lua = LuaUtil.getEctypeCombat();
				LuaValue[] value = lua.getField( "sailingTime" ).call( 1, CsvGen.getStarsPo( fleet.getBerthSnid() ), CsvGen.getStarsPo( snid ) );
				fleet.changeSail( snid, value[0].getInt(), new FightEctype( type, cnid, enid ) );
				throw new Exception( ErrorCode.SUCCEED.name() );
			}
			
			// 全军出击 ...
			Lua lua 		= LuaUtil.getEctypeCombat();
			LuaValue[] ret 	= lua.getField("arithmeticFight").call( 1, fleet.fighter(), ectype.fighter(), ectype.templet().btime, ectype.templet().maxran );
			int combatTime	= ret[0].getInt();
			int winRate 	= ret[1].getInt();
			int damaged 	= ret[2].getInt();
			int ammoExpend	= ret[3].getInt();
			
			// 算出胜负
			int rand 		= Random.get( 0, 10000 );
			byte iswin 		= (byte) (rand <= winRate ? 1 : 0);
			// 根据胜负算出奖励
			List<AwardInfo> awards = (iswin == 1 ? ectype.randomAward() : null);
			// 切换战斗状态
			fleet.changeCombat( snid, type, cnid, enid, combatTime, iswin, awards );
			// 计算舰队船的 战损 和 结算所有舰长的忠诚度
			List<ShipInfo> ships = fleet.getShips();
			for( ShipInfo ship : ships ){
				// 耐久消耗
				ship.addCurrentHp( damaged );
				// 弹药消耗
				if( Random.get( 0, 10000 ) <= ammoExpend )
					ship.toreduceAmmo( -1 );
				// 舰长忠诚度
				CaptainInfo capt = player.getDocks().getCaptain( ship.getCaptainUID() );
				if( capt.changeLoyalty( iswin == 1 ? 1 : -1 ) ){
					// 这里如果没有忠诚度了 就要删除掉
					player.getDocks().destroy( capt );
					ship.setCaptainUID( -1 );
				}
			}
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeInt( fid );
			status.buildTransformStream( response );
		}
		sendPackage( player.getCtx(), response );
	}
	
}

