package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;

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
		try {
			
			//TODO
			
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			response.writeShort(0);
			response.writeShort(0);
		}
		sendPackage( player.getCtx(), response );
	}

}
