package cn.xgame.net.event.all.pl.ship;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.classes.TeamAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;

/**
 * 查看队伍信息
 * @author deng		
 * @date 2015-10-23 下午5:23:30
 */
public class LookTeamEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte fid = data.readByte();
		
		List<IAxnCrew> crews = getCrews(player, fid);
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 512 );
		buffer.writeByte( crews.size() );
		for( IAxnCrew crew :crews ){
			Player temp = PlayerManager.o.getPlayerByTeam( crew.getUid() );
			buffer.writeByte( temp.isOnline() ? 1 : 0 );
			RW.writeString( buffer, temp.getUID() );
			RW.writeString( buffer, temp.getNickname() );
			FleetInfo fleet = temp.getFleets().getFleetInfo( ((TeamAxnCrew)crew).getFid() );
			int[] allDur = fleet.getAllDur();
			buffer.writeInt( allDur[0] );
			buffer.writeInt( allDur[1] );
			buffer.writeInt( fleet.getShips().get(0).getnId() );
			buffer.writeByte( fleet.getStatus().type().toNumber() );
			buffer.writeInt( fleet.getBerthSnid() );
		}
		sendPackage( player.getCtx(), buffer );
	}
	
	// 获取队伍成员
	private List<IAxnCrew> getCrews(Player player, byte fid) {
		FleetInfo fleet = player.getFleets().getFleetInfo(fid);
		if( fleet == null ) 
			return Lists.newArrayList();
		AxnInfo axn = ChatManager.o.axns().getAXNInfo( fleet.getAxnId() );
		if( axn == null ) 
			return Lists.newArrayList();
		return axn.getAxnCrews();
	}

}
