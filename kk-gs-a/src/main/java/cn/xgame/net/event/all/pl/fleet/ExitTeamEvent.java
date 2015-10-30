package cn.xgame.net.event.all.pl.fleet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.AxnControl;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.event.all.pl.update.Update_2301;
import cn.xgame.utils.Logs;

/**
 * 退出队伍
 * @author deng		
 * @date 2015-10-23 下午5:22:57
 */
public class ExitTeamEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte fid = data.readByte();
		
		AxnControl chatControl = ChatManager.o.axns();
		
		ErrorCode code  = null;
		AxnInfo axn		= null;
		try {
			
			FleetInfo fleet = player.getFleets().getFleetInfo( fid );
			if( fleet == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 获取频道
			axn = chatControl.getAXNInfo(fleet.getAxnId());
			if( axn == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 直接删除
			axn.removeCrew( player.getUID() );
			fleet.setAxnId(0);
			
			Logs.debug( player.getCtx(), "舰队"+fleet.getNo()+" 退出 队伍" );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( fid );
		}
		sendPackage( player.getCtx(), buffer );
		
		// 检测剩余队友是否都在线 
		if( code == ErrorCode.SUCCEED ){
			
			List<IAxnCrew> crews = axn.getAxnCrews();
			
			do{
				// 如果还有2个人以上 而且都在线 那么就不用解散
				if( axn.isHaveOnline() && crews.size() > 1 )
					break;
				
				// 下面执行 解散队伍
				for( IAxnCrew crew : crews ){
					Player temp = PlayerManager.o.getPlayerByTeam( crew.getUid() );
					FleetInfo fleet = temp.getFleets().getFleetInfo( axn.getAxnId() );
					fleet.setAxnId(0);
					if( temp.isOnline() ){
						((Update_2301)Events.UPDATE_2301.toInstance()).run( axn.getAxnId(), player.getUID(), temp );
					}else if( !temp.getFleets().isHaveTeam() ) {// 如果还有舰队有队伍 就不退出
						PlayerManager.o.exitByOffline( temp );
					}
				}
				
				chatControl.removeAxn( axn.getAxnId() );
			} while( false );
		}
	}
	
}
