package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;


import cn.xgame.a.player.ectype.EctypeControl;
import cn.xgame.a.player.ectype.info.ChapterInfo;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

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
		
		EctypeControl control = player.getEctypes();
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 1024 );
		
		// 瞭望副本
		List<ChapterInfo> outlooks = control.getOutlookEctype(snid);
		buffer.writeByte( outlooks.size() );
//		for( ChapterEctype o : general ){
//			buffer.writeByte( 1 );
//			buffer.writeInt( o.getSnid() );
//			o.buildTransformStream(buffer);
//			buffer.writeInt( endtime );
//			List<IEctype> ectypes = o.getEctypes();
//			buffer.writeByte( ectypes.size() );
//			for( IEctype x : ectypes ){
//				buffer.writeInt( x.getNid() );
//				Lua lua = LuaUtil.getEctypeCombat();
//				lua.getField( "arithmeticShowData" ).call( 0, o.getSnid(), x, fleet, buffer );
//			}
//		}
		
		// 偶发副本
		List<ChapterInfo> chances = control.getChanceEctype(snid);
		buffer.writeByte( chances.size() );
//		for( ChapterEctype o : normals ){
//			buffer.writeByte( 2 );
//			buffer.writeInt( o.getSnid() );
//			o.buildTransformStream(buffer);
//			buffer.writeInt( o.getEndtime() );
//			List<IEctype> ectypes = o.getEctypes();
//			buffer.writeByte( ectypes.size() );
//			for( IEctype x : ectypes ){
//				buffer.writeInt( x.getNid() );
//				Lua lua = LuaUtil.getEctypeCombat();
//				lua.getField( "arithmeticShowData" ).call( 0, o.getSnid(), x, fleet, buffer );
//			}
//		}
		sendPackage( player.getCtx(), buffer );
	}
	
}
