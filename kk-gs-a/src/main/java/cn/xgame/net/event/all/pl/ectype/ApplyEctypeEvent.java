package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;


import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.classes.TeamAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.ectype.EctypeControl;
import cn.xgame.a.player.ectype.info.ChapterInfo;
import cn.xgame.a.player.ectype.info.EctypeInfo;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.LuaUtil;

/**
 * 申请某个星球的副本信息
 * @author deng		
 * @date 2015-7-20 上午10:12:10
 */
public class ApplyEctypeEvent extends IEvent{
	
	@Override
	public void run( Player player, ByteBuf data ) throws IOException {
		
		byte fId = data.readByte();//舰队ID
		int snid = data.readInt();//星球ID
		
		// 如果没有舰船直接返回
		FleetInfo fleet 		= player.getFleets().getFleetInfo( fId );
		if( fleet.isEmpty() )
			return;
		
		try {
			EctypeControl control = player.getEctypes();
			
			ByteBuf buffer = buildEmptyPackage( player.getCtx(), 1024 );
			
			List<FleetInfo> allfleets = getAllFleets( player, fleet );
			// 获取玩家战斗力
			Fighter fighter = wrapFighter( allfleets );
						
			// 常规副本
			List<ChapterInfo> generals = control.getGeneralEctype(snid);
			buffer.writeShort( generals.size() );
			for( ChapterInfo o : generals ){
				buffer.writeInt( o.getSnid() );
				buffer.writeInt( o.getId() );
				buffer.writeByte( o.getQuestions().size() );
				for( int id : o.getQuestions() ){
					buffer.writeInt( id );
				}
				int startId = getFarthestSid( allfleets, o.getSnid() );
				List<EctypeInfo> guajiEctypes = o.getGuajiEctypes();
				buffer.writeByte( guajiEctypes.size() );
				for( EctypeInfo x : guajiEctypes ){
					buffer.writeByte( x.getLevel() );
					LuaUtil.getEctypeCombat().getField( "arithmeticShowData" ).call( 0, startId, o.getSnid(), x, fighter, buffer );
				}
				List<EctypeInfo> ectypes = o.getEctypes();
				buffer.writeByte( ectypes.size() );
				for( EctypeInfo x : ectypes ){
					buffer.writeByte( x.getLevel() );
					LuaUtil.getEctypeCombat().getField( "arithmeticShowData" ).call( 0, startId, o.getSnid(), x, fighter, buffer );
				}
			}
			
			// 偶发副本
			List<ChapterInfo> chances = control.getChanceEctype(snid);
			buffer.writeShort( chances.size() );
			for( ChapterInfo o : chances ){
				buffer.writeInt( o.getSnid() );
				buffer.writeInt( o.getId() );
				buffer.writeInt( o.getEndtime() );
				buffer.writeByte( o.getTimes() );
				buffer.writeByte( o.getQuestions().size() );
				for( int id : o.getQuestions() ){
					buffer.writeInt( id );
				}
				int startId = getFarthestSid( allfleets, o.getSnid() );
				List<EctypeInfo> ectypes = o.getEctypes();
				buffer.writeByte( ectypes.size() );
				for( EctypeInfo x : ectypes ){
					buffer.writeByte( x.getLevel() );
					LuaUtil.getEctypeCombat().getField( "arithmeticShowData" ).call( 0, startId, o.getSnid(), x, fighter, buffer );
				}
			}
			sendPackage( player.getCtx(), buffer );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取要到达目的星球 玩家中最远的星球
	 * @param allfleets
	 * @param snid
	 * @return
	 */
	private int getFarthestSid( List<FleetInfo> allfleets, int snid ) {
		int ret = snid;
		int temp = 0;
		for( FleetInfo fleet : allfleets ){
			int stime = LuaUtil.getEctypeCombat().getField( "getSailingTime" ).call( 1, fleet.getBerthSnid(), snid )[0].getInt();
			if( stime > temp ){
				temp 	= stime;
				ret 	= fleet.getBerthSnid();
			}
		}
		return ret;
	}

	/**
	 * 包装战斗者
	 * @param allfleets
	 * @return
	 */
	private Fighter wrapFighter( List<FleetInfo> allfleets ) {
		Fighter ret = new Fighter();
		for( FleetInfo fleet : allfleets ){
			Fighter temp = fleet.fighter();
			ret.hp += temp.hp;
			ret.addAtkattr( temp.attacks );
			ret.addDefattr( temp.defends );
		}
		return ret;
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
}
