package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
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
		
		int snid = data.readInt();
		int enid = data.readInt();
		int suid = data.readInt();
		
		Logs.debug( player, "申请结束副本 星球ID=" + snid + ", 副本ID=" + enid + ", 舰船UID=" + suid );
		
		try {
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
