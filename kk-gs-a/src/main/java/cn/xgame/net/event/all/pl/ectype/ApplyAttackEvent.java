package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.classes.TeamAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.ectype.info.ChapterInfo;
import cn.xgame.a.player.ectype.info.EctypeInfo;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.fleet.info.vote.EctypeVote;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.event.all.pl.update.Update_1290;
import cn.xgame.system.LXConstants;

/**
 * 请求出击
 * @author deng		
 * @date 2015-10-30 上午6:43:43
 */
public class ApplyAttackEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int snid 	= data.readInt();// 星球ID
		byte type	= data.readByte(); // 类型
		int cnid 	= data.readInt();// 章节ID
		byte ltype	= data.readByte();// 难度类型
		byte level	= data.readByte();// 难度
		byte fid 	= data.readByte();// 舰队ID
		
		ErrorCode code = null;
		FleetInfo fleet	= null;
		AxnInfo axn = null;
		ChapterInfo chapter = null;
		EctypeInfo ectype = null;
		try {
			// 获取舰队
			fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.isEmpty() || !fleet.isHover() || fleet.getAxnId() == -1 )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			axn = ChatManager.o.axns().getAXNInfo( fleet.getAxnId() );
			if( axn == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 获取副本信息
			chapter = getChapter( player, snid, type, cnid );
			ectype = chapter.getEctype(ltype, level);
			if( ectype == null )
				throw new Exception( ErrorCode.ECTYPE_NOTEXIST.name() );
			
			// 检测队友是否全部悬停
			checkIsAllHover( player, axn.getAxnCrews() );
			
			// 设置投票状态
			int statrtime = (int) (System.currentTimeMillis()/1000);
			fleet.changeStatus( StatusType.VOTE, statrtime, LXConstants.ECTYPE_VOTE_TIME, 
					Lists.newArrayList( player.getUID() ), new EctypeVote( player.getUID(), type, cnid, ltype, level ) );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 1024 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( fid );
			fleet.getStatus().buildTransformStream( buffer );
		}
		sendPackage( player.getCtx(), buffer );
		
		// 下面同步消息
		if( code == ErrorCode.SUCCEED ){
			List<FleetInfo> allfleets = getAllFleets( player, fleet );
			List<IAxnCrew> crews = axn.getAxnCrews();
			for( IAxnCrew crew : crews ){
				if( crew.getUid().equals( player.getUID() ) )
					continue;
				TeamAxnCrew x 	= (TeamAxnCrew) crew;
				Player o 		= PlayerManager.o.getPlayerByTeam( x.getUid() );
				if( x.isOnlineAndUpdate() )
					((Update_1290)Events.UPDATE_1290.toInstance()).run( o, player, fleet.getNo(), x.getFid(), allfleets, chapter, ltype, ectype );
			}
		}
		
	}

	/**
	 * 获取玩家包括组队的全部信息
	 * @param player
	 * @param fleet
	 * @return
	 */
	private List<FleetInfo> getAllFleets( Player player, FleetInfo fleet ) {
		List<FleetInfo> ret = Lists.newArrayList();
		// 如果有组队
		if( fleet.getAxnId() != -1 ){
			AxnInfo axn = ChatManager.o.axns().getAXNInfo( fleet.getAxnId() );
			List<IAxnCrew> crews = axn.getAxnCrews();
			for( IAxnCrew crew : crews ){
				if( crew.getUid().equals( player.getUID() ) )
					continue;
				TeamAxnCrew x 	= (TeamAxnCrew) crew;
				Player o 		= PlayerManager.o.getPlayerByTeam( x.getUid() );
				FleetInfo f 	= o.getFleets().getFleetInfo( x.getFid() );
				ret.add( f );
			}
		}
		ret.add( fleet );
		return ret;
	}
	
	/**
	 * 获取章节
	 * @param player
	 * @param snid
	 * @param type
	 * @param cnid
	 * @param level 
	 * @param ltype 
	 * @return
	 * @throws Exception 
	 */
	private ChapterInfo getChapter(Player player, int snid, byte type, int cnid ) throws Exception {
		
		switch ( type ) {
		case 1:// 常规副本
			IPlanet planet = WorldManager.o.getPlanet( snid );
			return planet.getChapter( cnid );
		case 2:// 偶发副本
			return player.getEctypes().getChapter( snid, cnid );
		}
		
		throw new Exception( ErrorCode.ECTYPE_NOTEXIST.name() );
	}
	
	/**
	 * 检测 队友是否全部悬停
	 * @param player
	 * @param crews
	 * @throws Exception
	 */
	private void checkIsAllHover(Player player, List<IAxnCrew> crews) throws Exception {
		for( IAxnCrew crew : crews ){
			if( crew.getUid().equals( player.getUID() ) )
				continue;
			TeamAxnCrew x 	= (TeamAxnCrew) crew;
			Player o 		= PlayerManager.o.getPlayerByTeam( x.getUid() );
			FleetInfo f 	= o.getFleets().getFleetInfo( x.getFid() );
			if( !f.isHover() )
				throw new Exception( ErrorCode.TEAM_NOTHOVER.name() );
		}
	}

}
