package cn.xgame.net.event.all.pl.update;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.player.ectype.info.ChapterInfo;
import cn.xgame.a.player.ectype.info.EctypeInfo;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;
import cn.xgame.net.netty.Netty.RW;

/**
 * 有人请求出击 通知
 * @author deng		
 * @date 2015-10-30 下午4:49:19
 */
public class Update_1290 extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	/**
	 * 通知
	 * @param accept
	 * @param player
	 * @param No 
	 * @param No1 
	 * @param allfleets
	 * @param chapter
	 * @param ltype 
	 * @param ectype
	 */
	public void run(Player accept, Player player, byte No, byte No1, List<FleetInfo> allfleets, ChapterInfo chapter, byte ltype, EctypeInfo ectype) {
		
		try {
			int startId = getFarthestSid( allfleets, chapter.getSnid() );
			Fighter fighter = wrapFighter( allfleets );
			
			ByteBuf buffer = buildEmptyPackage( accept.getCtx(), 130 );
			RW.writeString( buffer, player.getUID() );
			RW.writeString( buffer, player.getNickname() );
			buffer.writeByte( No );
			buffer.writeByte( No1 );
			buffer.writeInt( chapter.getId() );
			buffer.writeByte( ltype );
			buffer.writeByte( ectype.getLevel() );
			buffer.writeInt( chapter.getEndtime() );
			buffer.writeByte( chapter.getQuestions().size() );
			for( int id : chapter.getQuestions() ){
				buffer.writeInt( id );
			}
			LuaUtil.getEctypeCombat().getField( "arithmeticShowData" ).call( 0, startId, chapter.getSnid(), ectype, fighter, buffer );
			sendPackage( accept.getCtx(), buffer );
		} catch (Exception e) {
			Logs.error( "Update_1220 ", e );
		}
	}

	private Fighter wrapFighter(List<FleetInfo> allfleets) {
		Fighter ret = new Fighter();
		for( FleetInfo fleet : allfleets ){
			Fighter temp = fleet.fighter();
			ret.hp += temp.hp;
			ret.addAtkattr( temp.attacks );
			ret.addDefattr( temp.defends );
		}
		return ret;
	}

	private int getFarthestSid(List<FleetInfo> allfleets, int snid) {
		int ret = snid;
		int temp = 0;
		for( FleetInfo fleet : allfleets ){
			int stime = LuaUtil.getEctypeCombat().getField( "getSailingTime" ).call( 1, fleet.getBerthSnid(), snid, fleet.toShipDatas() )[0].getInt();
			if( stime > temp ){
				temp 	= stime;
				ret 	= fleet.getBerthSnid();
			}
		}
		return ret;
	}
	
	

}
