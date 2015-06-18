package cn.xgame.net.event.all.pl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import x.javaplus.util.ErrorCode;
import x.javaplus.util.Util.Key;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.system.Constants;
import cn.xgame.net.event.IEvent;
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
		
		String UID 	= RW.readString(data);
		String key	= RW.readString(data);
		
		ErrorCode code 	= null;
		Player player 	= null;
		try {
			
			// 验证key是否正确
			if( !Key.verify( key, UID+Constants.PUBLICKEY ) )
				throw new Exception( ErrorCode.LKEY_ERROR.name() );
			
			// 获取玩家信息
			player	= PlayerManager.o.login( ctx, UID );
			
			code	= ErrorCode.SUCCEED;
		} catch (Exception e) {
			code	= ErrorCode.valueOf( e.getMessage() );
		}
		
		
		ByteBuf response = buildEmptyPackage( ctx, 1024 );
		response.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			// 基本数据
			player.buildTransformStream( response );
			// 背包数据
			player.getBags().buildTransformStream( response );
		}
		sendPackage( ctx, response );
	}


}
