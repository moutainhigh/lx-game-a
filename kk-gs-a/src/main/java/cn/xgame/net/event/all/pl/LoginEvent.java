package cn.xgame.net.event.all.pl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;
import x.javaplus.util.Util.Key;
import x.javaplus.util.Util.Time;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.dock.capt.CaptainInfo;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.fleet.FleetControl;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.manor.info.IBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;
import cn.xgame.system.LXConstants;
import cn.xgame.utils.Logs;
import cn.xgame.net.netty.Netty.RW;

/**
 * 玩家登录 
 * @author deng		
 * @date 2015-6-15 下午4:49:21
 */
public class LoginEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	public void run( ChannelHandlerContext ctx, ByteBuf data ) throws IOException {
		
		Time.beginTimer();
		
		String UID 	= RW.readString(data);
		String key	= RW.readString(data);
		
		ErrorCode code 	= null;
		Player player 	= null;
		HomePlanet home = null;
		try {
			
			// 验证key是否正确
			if( !Key.verify( key, UID+LXConstants.PUBLICKEY ) )
				throw new Exception( ErrorCode.LKEY_ERROR.name() );
			
			// 获取玩家信息
			player 	= PlayerManager.o.login( ctx, UID );
			
			// 获取母星 信息
			home 	= WorldManager.o.getHPlanetInPlayer( player );
			
			code	= ErrorCode.SUCCEED;
		} catch (Exception e) {
			code	= ErrorCode.valueOf( e.getMessage() );
			if( code == null ){
				code = ErrorCode.OTHER_ERROR;
				Logs.error( "玩家登录错误 " + e.getMessage() );
			}
		}
		
		ByteBuf buffer = buildEmptyPackage( ctx, 1024 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			FleetControl fleetCtr = player.getFleets();
			List<FleetInfo> fleets = fleetCtr.getFleet();
			for( FleetInfo fleet : fleets )// 发送数据之前先处理一下舰队状态
				fleet.updateStatus( player );
			
			// 基本数据
			player.buildTransformStream( buffer );
			// 领地信息
			buffer.writeInt( player.getManors().getNid() );
			List<IBuilding> builds = player.getManors().getBuilds();
			buffer.writeByte( builds.size() );
			for( IBuilding o : builds ){
				buffer.writeInt( o.getNid() );
				buffer.writeByte( o.getIndex() );
				buffer.writeByte( o.getStatus().ordinal() );
				buffer.writeInt( o.getEndtime() );
			}
			// 母星数据
			home.buildTransformStream( buffer );
			home.putPlyaerInfo( player, buffer );
			player.getDepots( home.getId() ).buildTransformStream( buffer );
			// 舰长数据
			List<CaptainInfo> capts = player.getDocks().getCabin();
			buffer.writeByte( capts.size() );
			for( CaptainInfo capt : capts )
				capt.buildTransformStream(buffer);
			// 舰船数据
			List<ShipInfo> ships = player.getDocks().getApron();
			buffer.writeByte( ships.size() );
			for( ShipInfo ship : ships ){
				ship.buildTransformStream(buffer);
				FleetInfo fleetInfo = fleetCtr.getFleetInfo( ship );
				buffer.writeByte( fleetInfo == null ? 0 : fleetInfo.getNo() );
			}
			// 舰队数据
			buffer.writeByte( fleets.size() );
			for( FleetInfo fleet : fleets ){
				fleet.buildTransformStream(player,buffer);
			}
			// 已接任务列表 and 可接任务列表
			player.getTasks().buildTransformStream(buffer);
			// 聊天频道信息
			player.getChatAxns().buildTransformStream(buffer);
			// 新手引导
			RW.writeString(buffer, player.getGuideStatus());
		}
		sendPackage( ctx, buffer );
		
		
		// 这里表示 已经登录成功了
		if( code == ErrorCode.SUCCEED ){
			
			// 记录最后一次登录的服务器ID
			player.rLastGsid();
		}
		
		Logs.debug( ctx, "登录游戏 " + code + ", 逻辑耗时：" + Time.endTimer() + "毫秒" );
	}

}
