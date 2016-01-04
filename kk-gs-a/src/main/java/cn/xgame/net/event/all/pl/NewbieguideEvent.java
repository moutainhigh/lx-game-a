package cn.xgame.net.event.all.pl;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 保存新手引导状态值
 * @author deng		
 * @date 2015-11-20 上午11:47:57
 */
public class NewbieguideEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		String guidestatus = RW.readString(data);
		player.setGuideStatus(guidestatus);
		Logs.debug(player.getCtx(), "保存引导 =" + guidestatus);
	}

}
