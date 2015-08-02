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
 * 玩家登录 
 * @author deng		
 * @date 2015-6-15 下午4:49:21
 */
public class LoginEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
	}

	public void run( ChannelHandlerContext ctx, ByteBuf data ) throws IOException {
		
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
		}
		
		
		ByteBuf response = buildEmptyPackage( ctx, 1024 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			// 基本数据
			player.buildTransformStream( response );
			// 发送自己母星数据
			home.buildTransformStream( response );
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
		}
		
	}


}
