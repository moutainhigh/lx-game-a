package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;


import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 回答是否同意组队
 * @author deng		
 * @date 2015-8-2 下午8:35:58
 */
public class AnswerTeamEvent extends IEvent{

//	private final AxnControl chatControl = ChatManager.o.getChatControl();
	
	
	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
//		String iuid = RW.readString(data);
//		int isuid 	= data.readInt();
//		int msuid	= data.readInt();
//		
//		ErrorCode code 	= null;
//		AxnInfo axn		= null;
//		Player ipla		= null;
//		ShipInfo mship	= null;
//		try {
//			
//			// 如果不同意 直接返回
//			if( msuid == -1 )
//				throw new Exception( ErrorCode.REJECTPARTY.name() );
//			
//			ipla = PlayerManager.o.getPlayerFmOnline(iuid);
//			if( ipla == null )
//				throw new Exception( ErrorCode.PLAYER_NOTEXIST.name() );
//			// 判断玩家是否邀请过该玩家
//			ShipInfo iship = ipla.getDocks().getShip(isuid);
//			if( !iship.getTemprecord().isHaveInviteTeam( player ) )
//				throw new Exception( ErrorCode.TEAM_TIMEOUT.name() );
//			
//			// 判断舰船是否可以出战
//			mship = player.getDocks().getShip(msuid);
//			if( !mship.isCanFighting() )
//				throw new Exception( ErrorCode.SHIP_NOTFIGHTING.name() );
//			
//			// 将玩家加入频道
//			axn = chatControl.getAXNInfo( iship.getTeamId() );
//			if( axn == null ){
//				axn = chatControl.createAxn( ChatType.TEMPAXN );
//				axn.appendTeamCrew( ipla, iship );
//				iship.setTeamId( axn.getAxnId() );
//			}
//			// 设置频道ID
//			axn.appendTeamCrew( player, mship );
//			mship.setTeamId( axn.getAxnId() );
//			
//			// 完成后删除记录
//			iship.getTemprecord().removeInviteTeam( player );
//			code = ErrorCode.SUCCEED;
//		} catch (Exception e) {
//			code = ErrorCode.valueOf( e.getMessage() );
//		}
//		
//		ByteBuf response = buildEmptyPackage( player.getCtx(), 1024 );
//		response.writeShort( code.toNumber() );
//		if( code == ErrorCode.SUCCEED ){
//			response.writeInt( axn.getAxnId() );
//			List<IAxnCrew> crews = axn.getAxnCrews();
//			byte size = 0;
//			response.writeByte( size );// 这里要把自己减去
//			for( IAxnCrew crew : crews ){
//				if( crew.getUid().equals( player.getUID() ) )
//					continue;
//				TeamAxnCrew team = (TeamAxnCrew)crew;
//				team.buildTransformStream(response);
//				++size;
//			}
//			response.setByte( 11, size );
//		}
//		sendPackage( player.getCtx(), response );
//		
//		// 同步给 邀请者
//		if( code == ErrorCode.SUCCEED )
//			((Update_3021)Events.UPDATE_3021.getEventInstance()).run( ipla, isuid, 1, axn.getAxnId(), player, mship );
//		if( code == ErrorCode.REJECTPARTY )
//			((Update_3021)Events.UPDATE_3021.getEventInstance()).run( ipla, isuid, 0, axn.getAxnId(), player, mship );
	}

}
