package cn.xgame.net.event.all.pl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import x.javaplus.util.ErrorCode;
import x.javaplus.util.Util.Key;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.system.LXConstants;

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
			home 	= WorldManager.o.allotHomePlanet( ctx );
			
			// 获取玩家信息
			player	= PlayerManager.o.create( ctx, UID, headIco, adjutantId, name, home.getId() );
			
			// 加入星球
			home.appendPlayer(player);
			
			// 获取副本信息
			player.updateEctype();
			
			code	= ErrorCode.SUCCEED;
		} catch (Exception e) {
			code	= ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf response = buildEmptyPackage( ctx, 6 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			// 基本数据
			player.buildTransformStream( response );
			// 发送自己母星数据
			home.buildTransformStream( response );
			home.putPlyaerInfo(player, response);
			// 舰长数据
			player.getCaptains().buildTransformStream(response);
			// 聊天频道信息
			player.getChatAxns().buildTransformStream(response);
		}
		sendPackage( ctx, response );
	
		// 这里表示 已经登录成功了
		if( code == ErrorCode.SUCCEED ){
			
			// 记录最后一次登录的服务器ID
			player.rLastGsid();
			
			// 开始记录登录计时副本
			player.getEctypes().startRLoginTime();
			
			// 保存数据库一次
//			PlayerManager.o.update(player);
		}
	}

}
