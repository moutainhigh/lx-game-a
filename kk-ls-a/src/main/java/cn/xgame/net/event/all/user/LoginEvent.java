package cn.xgame.net.event.all.user;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.system.ESB;
import cn.xgame.logic.user.UserManager;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.Logs;

/**
 * 用户登录
 * @author deng		
 * @date 2015-6-12 上午10:54:25
 */
public class LoginEvent extends IEvent {

	@Override
	public void run( ChannelHandlerContext ctx, ByteBuf data ) throws IOException {
		
		// 平台
		ESB esb = ESB.fromNum( data.readByte() );
		if( esb == null ){
			Logs.error( "LoginEvent 平台为NULL" );
			return;
		}
		
		switch( esb ){
		case APPLE:
			appleLogin( ctx, data );
		break;
		case ANDROID:
			androidLogin( ctx, data );
		break;
		}
		
	}

	// 苹果 登录
	private void appleLogin( ChannelHandlerContext ctx, ByteBuf data ) throws IOException {
	
		// 账号
		String account 	= RW.readString(data);
		// 密码
		String password	= RW.readString(data);
		
		String UID		= null;
		ErrorCode code 	= null;
		try {
			UID			= UserManager.o.getUID( account, password );
			code		= ErrorCode.SUCCEED;
		} catch ( Exception e ) {
			code		= ErrorCode.valueOf( e.getMessage() );
		}
		
		// 
		ByteBuf respond = buildEmptyPackage( ctx, 16 );
		respond.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			RW.writeString( respond, UID );
		}
		sendPackage( ctx, respond );
		
	}
	
	// 安卓 登录
	private void androidLogin( ChannelHandlerContext ctx, ByteBuf data ) {
		
		
		
	}
}
