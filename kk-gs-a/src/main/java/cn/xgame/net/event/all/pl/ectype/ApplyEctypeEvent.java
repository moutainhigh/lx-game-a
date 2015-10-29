package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;


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
				List<EctypeInfo> guajiEctypes = o.getGuajiEctypes();
				buffer.writeByte( guajiEctypes.size() );
				for( EctypeInfo x : guajiEctypes ){
					buffer.writeByte( x.getLevel() );
					LuaUtil.getEctypeCombat().getField( "arithmeticShowData" ).call( 0, o.getSnid(), x, fleet, buffer );
				}
				List<EctypeInfo> ectypes = o.getEctypes();
				buffer.writeByte( ectypes.size() );
				for( EctypeInfo x : ectypes ){
					buffer.writeByte( x.getLevel() );
					LuaUtil.getEctypeCombat().getField( "arithmeticShowData" ).call( 0, o.getSnid(), x, fleet, buffer );
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
				List<EctypeInfo> ectypes = o.getEctypes();
				buffer.writeByte( ectypes.size() );
				for( EctypeInfo x : ectypes ){
					buffer.writeByte( x.getLevel() );
					LuaUtil.getEctypeCombat().getField( "arithmeticShowData" ).call( 0, o.getSnid(), x, fleet, buffer );
				}
			}
			sendPackage( player.getCtx(), buffer );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
