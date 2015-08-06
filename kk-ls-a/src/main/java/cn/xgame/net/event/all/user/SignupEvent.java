package cn.xgame.net.event.all.user;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import x.javaplus.util.ErrorCode;
import x.javaplus.util.Util.Key;

import cn.xgame.logic.user.UserManager;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.system.Constants;

/**
 * 用户注册
 * @author deng		
 * @date 2015-6-12 下午1:41:26
 */
public class SignupEvent extends IEvent {

	@Override
	public void run(ChannelHandlerContext ctx, ByteBuf data) throws IOException {
		
		// 账号
		String account 	= RW.readString(data);
		// 密码
		String password	= RW.readString(data);
		
		ErrorCode code	= null;
		String UID		= null;
		String key		= null;
		try {
			UID			= UserManager.o.create( account, password );
			key			= Key.generateKey( UID+Constants.PUBLICKEY );
			code		= ErrorCode.SUCCEED;
		} catch (Exception e) {
			code		= ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf respond = buildEmptyPackage( ctx, 16 );
		respond.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			RW.writeString( respond, UID );
			RW.writeString( respond, key );
		}
		sendPackage( ctx, respond );
		
	}

}
