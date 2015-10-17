package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.data.tech.TechControl;
import cn.xgame.a.world.planet.data.tech.UnTechs;
import cn.xgame.a.world.planet.data.vote.VotePlayer;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.a.world.planet.home.o.Syn;
import cn.xgame.config.o.TechPo;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;

/**
 * 参与科技投票
 * @author deng		
 * @date 2015-7-2 上午11:02:51
 */
public class ParticipateTechVoEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		int nid 		= data.readInt();
		byte isAgree 	= data.readByte();
		
		ErrorCode code = null;
		
		try {
			// 获取对应星球  - 这里暂时 默认在母星发起投票
			HomePlanet planet = WorldManager.o.getHPlanetInPlayer(player);
			
			// 判断是否有权限投票 只要有话语权都可以投票
			Child child = planet.getChild( player.getUID() );
			if( child == null || child.getPrivilege() == 0 )
				throw new Exception( ErrorCode.NOT_PRIVILEGE.name() );

			// 判断是否已经参与投票了
			TechControl techmanage = planet.getTech();
			UnTechs unTech = techmanage.getVoTech( nid );
			if( unTech == null )
				throw new Exception( ErrorCode.VOTE_NOTEXIST.name() );
			
			// 这里先将玩家已经投过的票清除
			unTech.getVote().purgeVote( child );
			
			// 设置投票
			byte status = unTech.getVote().setIsAgrees( new VotePlayer( child ), isAgree );
			// 说明投票完成
			if( status != -1 ){
				if( status == 1 ) // 同意建筑
					startStudy( planet, unTech.templet(), unTech.getVote().getSponsorUid() );
				if( status == 0 ){ // 反对建筑
					//...暂时没有处理
				}
				// 最后不管怎样都要删掉的
				techmanage.removeVoTech( unTech );
				
				// 同步
				Syn.tech( planet.getPeoples(), status+3, unTech );
			}else{
				Syn.tech( planet.getPeoples(), 2, unTech );
			}
			
			Logs.debug( player.getCtx(), "参与科技投票 当前票数 " + unTech.getVote() + " at=" + nid );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
		
	}

	private void startStudy( HomePlanet planet, TechPo templet, String sprUid ) {
		
		int endtime = -1;
		
		// 判断资源是否够 - 开始扣资源
		if( planet.getDepotControl().deductProp( templet.needres ) )
			endtime = (int) (System.currentTimeMillis()/1000) + templet.needtime;
		
		UnTechs unTech = new UnTechs( templet );
		unTech.setEndtime( endtime );
		
		// 放入建筑中 列表
		planet.getTech().appendUnTech( unTech );
		// 设置发起人的通过数
		Child sponsor = planet.getChild( sprUid );
		if( sponsor != null )
			sponsor.addPasss( 1 );
		
		Logs.debug( "星球"+planet.getId()+"开始研究科技 " + templet.id );
	}

}
