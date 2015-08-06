package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import cn.xgame.a.player.captain.o.CaptainInfo;
import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 同意和拒绝组队 通知
 * @author deng		
 * @date 2015-8-2 下午11:15:07
 */
public class Update_3021 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}
	
	/**
	 * 通知
	 * @param ipla 邀请者
	 * @param isuid 邀请者舰船UID
	 * @param isAgree 是否同意 1.同意 0.不同意
	 * @param axnId 频道ID
	 * @param player 被邀请者
	 * @param ship 被邀请者舰船
	 */
	public void run( Player ipla, int isuid, int isAgree, int axnId, Player player, ShipInfo ship ) {
//		isAgree 	: Byte( 是否同意 1.同意 0.拒绝 )
//		name 		: String( 被邀请人名字 )
//		suid 		: Int( 邀请人使用的舰船UID )
//		if( isAgree == 1 ){
//			axnId 		: Int( 组队频道ID )
//			uid			: String( 被邀请人UID )
//			headIco 	: Int( 被邀请人头像ID )
//			shipNid		: Int( 被邀请人舰船NID )
//			shipUid		: Int( 被邀请人舰船UID )
//			snid		: Int( 舰船所在星球ID )
//			size		: Byte( 舰船装备个数 )
//			For( < size ){
//			uid		: Int( 道具UID )
//			nid		: Int( 道具NID )
//			count	: Int( 道具数量 )
//			}
//			capUid		: Int( 舰长UID )
//			capNid		: Int( 舰长NID )
//			equNid		: Int( 舰长装备NID )
//		}
		try {
			ByteBuf response = buildEmptyPackage( ipla.getCtx(), 30 );
			
			response.writeByte( isAgree );
			RW.writeString( response, player.getNickname() );
			response.writeInt(isuid);
			if( isAgree == 1 ){
				response.writeInt(axnId);
				RW.writeString( response, player.getUID() );
				response.writeInt( player.getHeadIco() );
				response.writeInt( ship.getuId() );
				response.writeInt( ship.getnId() );
				response.writeInt( ship.getStatus().getCurrentSnid() );
				ship.getEquips().putBaseBuffer(response);
				CaptainInfo captain = player.getCaptains().getCaptain( ship.getCaptainUID() );
				if( captain != null )
					captain.buildTransformStream(response);
			}

			sendPackage( ipla.getCtx(), response );
		} catch (Exception e) {
			Logs.error( "Update_3021 ", e );
		}
	}

}
