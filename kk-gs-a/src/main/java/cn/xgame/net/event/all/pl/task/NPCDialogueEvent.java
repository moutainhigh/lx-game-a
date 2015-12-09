package cn.xgame.net.event.all.pl.task;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.task.classes.ConType;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * NPC对话
 * @author deng		
 * @date 2015-12-9 上午11:12:07
 */
public class NPCDialogueEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int sid = data.readInt();
		int npcid = data.readInt();
		
		player.getTasks().execute( ConType.DUIHUA, sid, npcid );
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 2 );
		buffer.writeShort( ErrorCode.SUCCEED.toNumber() );
		sendPackage( player.getCtx(), buffer );
	}

}
