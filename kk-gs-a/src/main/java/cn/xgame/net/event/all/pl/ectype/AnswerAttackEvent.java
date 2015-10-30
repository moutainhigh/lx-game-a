package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.classes.TeamAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.fleet.info.status.VoteStatus;
import cn.xgame.a.player.fleet.info.vote.EctypeVote;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.event.all.pl.update.Update_1221;
import cn.xgame.net.netty.Netty.RW;

/**
 * 是否同意出击
 * @author deng		
 * @date 2015-10-30 下午5:09:52
 */
public class AnswerAttackEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		String UID = RW.readString(data);
		byte sfid = data.readByte();
		byte fid = data.readByte();
		byte isAgree = data.readByte();
		
		ErrorCode code = null;
		FleetInfo fleet	= null;
		try {
			Player sender = PlayerManager.o.getPlayerByTeam(UID);
			if( sender == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			// 请求人舰队
			FleetInfo senderFleet = sender.getFleets().getFleetInfo(sfid);
			// 获取舰队
			fleet = player.getFleets().getFleetInfo(fid);
			if( senderFleet == null || fleet == null || fleet.isEmpty() || !fleet.isHover() || fleet.getAxnId() == -1 )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			if( !senderFleet.isVote() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			VoteStatus status = (VoteStatus) senderFleet.getStatus();
			EctypeVote vote = (EctypeVote) status.getVtype();
			
			// 如果不同意
			if( isAgree == 0 ){
				
				
				
				throw new Exception( ErrorCode.REJECTPARTY.name() );
			}

			// 添加到同意列表中
			status.getAgrees().add( player.getUID() );
			
			// 设置投票状态
			fleet.changeStatus( StatusType.VOTE, status.getStarttime(), status.getConttime(), 
					status.getAgrees(), new EctypeVote( UID, vote.getEtype(), vote.getChapterId(), vote.getLtype(), vote.getLevel() ) );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte(fid);
			fleet.getStatus().buildTransformStream(buffer);
		}
		sendPackage( player.getCtx(), buffer );
		
		// 同步其他玩家
		if( code == ErrorCode.SUCCEED || code == ErrorCode.REJECTPARTY ){
			AxnInfo axnInfo = ChatManager.o.axns().getAXNInfo( fleet.getAxnId() );
			List<IAxnCrew> crews = axnInfo.getAxnCrews();
			for( IAxnCrew crew : crews ){
				if( crew.getUid().equals( player.getUID() ) )
					continue;
				TeamAxnCrew x 	= (TeamAxnCrew) crew;
				
				// 同意的话 添加到同意列表
				if( isAgree == 1 ){
					Player o 		= PlayerManager.o.getPlayerByTeam( x.getUid() );
					FleetInfo f 	= o.getFleets().getFleetInfo( x.getFid() );
					
					VoteStatus status = (VoteStatus) f.getStatus();
					
					status.getAgrees().add( player.getUID() );
				} 
				
				if( x.isOnlineAndUpdate() )
					((Update_1221)Events.UPDATE_1221.toInstance()).run( x.getSocket(), x.getFid(), player.getUID(), isAgree );
			}
			
		}
	}

}
