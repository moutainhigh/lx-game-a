package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;
import x.javaplus.util.Util.Random;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;

import cn.xgame.a.combat.Fighter;
import cn.xgame.a.player.ectype.IEctype;
import cn.xgame.a.player.ship.o.ShipInfo;
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

		int snid = data.readInt();
		int enid = data.readInt();
		int suid = data.readInt();
		
		Logs.debug( player, "申请攻打副本 星球ID=" + snid + ", 副本ID=" + enid + ", 舰船UID=" + suid );
		
		ErrorCode code = null;
		byte isWin = 0;
		try {
			
			// 判断副本是否可以打
			IEctype ectype = player.getEctypes().getEctype( snid, enid );
			if( ectype == null )
				throw new Exception( ErrorCode.ECTYPE_NOTEXIST.name() );
			
			// 检测检测是否在该星球
			// TODO
			
			// 判断该船是否可以战斗
			ShipInfo ship = player.getDocks().getShip(suid);
			if( !ship.isCanCombat() )
				throw new Exception( ErrorCode.SHIP_CANNOT_FIGHT.name() );
			
			Fighter att = new Fighter( player, ship );// 攻击者
			Fighter def = new Fighter( ectype );// 防御者
			try {
				
				Lua lua = LuaUtil.getEctypeCombat();
				// 攻击者 防御者 基础战斗时间 胜率上限
				LuaValue[] ret = lua.getField( "oneToOneCombat" ).call( 2, att, def, ectype.template().btime, 10000 );
				int winRate = ret[1].getInt();
				
				int rand = Random.get( 0, 10000 );
				isWin = (byte) (rand <= winRate ? 1 : 0);
				
			} catch (Exception e) {
				Logs.error( player, "副本战斗错误 ", e );
				isWin = 0;
			}
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeByte( isWin );
			response.writeShort(0);
			response.writeShort(0);
		}
		sendPackage( player.getCtx(), response );
	}

}
