package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;


import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.AxnControl;
import cn.xgame.a.chat.axn.classes.ChatType;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.classes.TeamAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.event.all.pl.update.Update_3021;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 回答是否同意组队
 * @author deng		
 * @date 2015-8-2 下午8:35:58
 */
public class AnswerTeamAxnEvent extends IEvent{

	private final AxnControl chatControl = ChatManager.o.axns();
	
	
	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte isAgree= data.readByte();
		String iuid = RW.readString(data);
		byte ifid 	= data.readByte();
		byte mfid	= data.readByte();
		
		ErrorCode code 	= null;
		AxnInfo axn		= null;
		Player sponsor		= null;
		try {
			// 获取邀请人
			sponsor = PlayerManager.o.getPlayerFmOnline(iuid);
			if( sponsor == null )
				throw new Exception( ErrorCode.PLAYER_NOTEXIST.name() );

			// 如果不同意 直接返回
			if( isAgree == 0 )
				throw new Exception( ErrorCode.REJECTPARTY.name() );
			
			// 获取自己舰队
			FleetInfo mfleet = player.getFleets().getFleetInfo(mfid);
			if( mfleet == null || mfleet.isEmpty() || !mfleet.isHover() )
				throw new Exception( ErrorCode.SHIP_NOTLEISURE.name() );
			// 这里证明已经有队伍了
			if( mfleet.getAxnId() != -1 )
				throw new Exception( ErrorCode.SHIP_ISHAVETEAM.name() );
			
			// 获取邀请人舰队
			FleetInfo ifleet = sponsor.getFleets().getFleetInfo(ifid);
			if( ifleet == null || ifleet.isEmpty() || !ifleet.isHover() )
				throw new Exception( ErrorCode.SHIP_NOTLEISURE.name() );
			// 将玩家加入频道
			axn = chatControl.getAXNInfo( ifleet.getAxnId() );
			if( axn == null ){
				axn = chatControl.createAxn( ChatType.TEAM );
				axn.appendTeamCrew( sponsor, ifid );
				ifleet.setAxnId( axn.getAxnId() );
			}else if( axn.isMaxmember() ) {
				throw new Exception( ErrorCode.AXN_MAXMEMBER.name() );
			}
			// 设置频道ID
			axn.appendTeamCrew( player, mfid );
			mfleet.setAxnId( axn.getAxnId() );
			
			Logs.debug( player.getCtx(), (isAgree == 1 ? "同意" : "拒绝") + " <" + sponsor.getNickname() + "> 的群聊邀请" );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		// 获取频道玩家列表
		List<IAxnCrew> crews = axn.getAxnCrews();
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 1024 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( mfid );
			buffer.writeInt( axn.getAxnId() );
			buffer.writeByte( crews.size()-1 );
			for( IAxnCrew crew : crews ){
				if( crew.getUid().equals( player.getUID() ) )
					continue;
				crew.putBuffer(buffer);
			}
		}
		sendPackage( player.getCtx(), buffer );
		
		// 只有同意才同步给所有队友
		if( code == ErrorCode.SUCCEED){
			
			for( IAxnCrew crew : crews ){
				if( crew.getUid().equals( player.getUID() ) )
					continue;
				Player to = PlayerManager.o.getPlayerFmOnline( crew.getUid() );
				if( to == null ) 
					continue;
				TeamAxnCrew team = (TeamAxnCrew)crew;
				
				((Update_3021)Events.UPDATE_3021.toInstance()).run( isAgree, player, team.getFid(), to, axn.getAxnId() );
			}
			
			ChatManager.o.axns().update(axn);
		}
		// 如果是拒绝那么直给邀请者回复
		if( code == ErrorCode.REJECTPARTY ){
			((Update_3021)Events.UPDATE_3021.toInstance()).run( isAgree, player, ifid, sponsor, 0 );
		}
		
	}

}
