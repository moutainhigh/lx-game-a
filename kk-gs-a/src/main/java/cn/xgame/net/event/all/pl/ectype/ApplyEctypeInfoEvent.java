package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;

import cn.xgame.a.ectype.combat.Fighter;
import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.StarsPo;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

/**
 * 申请副本详细信息
 * @author deng		
 * @date 2015-8-3 上午3:13:45
 */
public class ApplyEctypeInfoEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
//		int snid = data.readInt();
//		int enid = data.readInt();
//		int suid = data.readInt();
//		
//		ErrorCode code 	= null;
//		int sailTime	= 0;
//		int combatTime	= 0;
//		int winRate 	= 0;
//		try {
//			
//			IEctype ectype = player.getEctypes().getEctype( snid, enid );
//			if( ectype == null )
//				throw new Exception( ErrorCode.ECTYPE_NOTEXIST.name() );
//			
//			// 这里判断组队信息
//			// TODO
//			
//			ShipInfo ship = player.getDocks().getShipOfException(suid);
//			
//			Fighter att = new Fighter( player, ship );// 攻击者
//			Fighter def = new Fighter( ectype );// 防御者
//			StarsPo cur	= CsvGen.getStarsPo( ship.getStatus().getCurrentSnid() );
//			StarsPo to 	= CsvGen.getStarsPo( snid );
//			try {
//				Lua lua = LuaUtil.getEctypeCombat();
//				// 攻击者 防御者 基础战斗时间 胜率上限
//				LuaValue[] ret = lua.getField( "ectypeDetailInfo" ).call( 3, att, def, 
//						ectype.templet().btime, ectype.templet().maxran,
//						cur, to
//						);
//				sailTime	= ret[0].getInt();
//				combatTime	= ret[1].getInt();
//				winRate 	= ret[2].getInt();
//				
//			} catch (Exception e) {
//				Logs.error( player, "副本战斗错误 ", e );
//			}
//			code = ErrorCode.SUCCEED;
//		} catch (Exception e) {
//			code = ErrorCode.valueOf( e.getMessage() );
//		}
//		
//		ByteBuf response = buildEmptyPackage( player.getCtx(), 11 );
//		response.writeShort( code.toNumber() );
//		if( code == ErrorCode.SUCCEED ){
//			response.writeInt(sailTime);
//			response.writeInt(combatTime);
//			response.writeByte(winRate/100);
//		}
//		sendPackage( player.getCtx(), response );
		
	}

}
