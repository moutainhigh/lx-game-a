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
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;
import cn.xgame.system.LXConstants;
import cn.xgame.utils.Logs;
import cn.xgame.net.netty.Netty.RW;

/**
 * 创建角色
 * @author deng		
 * @date 2015-6-30 上午10:12:37
 */
public class CreateEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	public void run( ChannelHandlerContext ctx, ByteBuf data ) throws IOException {
		
		Time.beginTimer();
		
		String UID 	= RW.readString(data);
		String key	= RW.readString(data);
		int headIco	= data.readInt();
		String name = RW.readString(data);
		int adjutantId = data.readInt();
		
		ErrorCode code 	= null;
		Player player 	= null;
		HomePlanet home = null;
		try {
			
			// 验证key是否正确
			if( !Key.verify( key, UID+LXConstants.PUBLICKEY ) )
				throw new Exception( ErrorCode.LKEY_ERROR.name() );
			
			// 分配母星
			home = WorldManager.o.getHomePlanet( LXConstants.INIT_HOMESTAR_NID );
			
			// 获取玩家信息
			player	= PlayerManager.o.create( ctx, UID, headIco, adjutantId, name, home.getId() );
			
			home.appendPlayer(player);
			
			// 创建一个舰队
			player.getFleets().createFleet( home.getId() );
			player.getFleets().createFleet( home.getId() );
			
			ShipInfo shipInfo = player.getDocks().getApron().get(0);
			FleetInfo fleetInfo = player.getFleets().getFleet().get(0);
			fleetInfo.addShip(shipInfo);
//			fleetInfo.setBerthSnid( 2007 );
			
			code	= ErrorCode.SUCCEED;
		} catch (Exception e) {
			code	= ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( ctx, 1024 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			FleetControl fleetCtr = player.getFleets();
			// 基本数据
			player.buildTransformStream( buffer );
			// 发送自己母星数据
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
			List<FleetInfo> fleets = fleetCtr.getFleet();
			buffer.writeByte( fleets.size() );
			for( FleetInfo fleet : fleets ){
				fleet.buildTransformStream(player,buffer);
			}
			// 已接任务列表 and 可接任务列表
			player.getTasks().buildTransformStream(buffer);
			// 聊天频道 这里强制发0
			buffer.writeByte( 0 );
			// 新手引导
			RW.writeString(buffer, player.getGuideStatus());
		}
		sendPackage( ctx, buffer );
	
		// 这里表示 已经登录成功了
		if( code == ErrorCode.SUCCEED ){
			
			// 记录最后一次登录的服务器ID
			player.rLastGsid();
			
			// 保存数据库一次
//			PlayerManager.o.update(player);
		}
		
		Logs.debug( ctx, "创建角色 " + code + ", 逻辑耗时：" + Time.endTimer() + "毫秒" );
	}

}
