package cn.xgame.net;

import cn.xgame.net.event.Events;
import cn.xgame.net.netty.Netty.IP;
import cn.xgame.utils.Logs;
import io.netty.channel.ChannelHandlerContext;

public class Net {
	public void packageRun( ChannelHandlerContext ctx, byte packageNo, byte[] data) {
		
		Events event 		= Events.fromNum( packageNo );
		
		Logs.debug( "申请包 " + packageNo );
		try{
			if( event == null ) throw new Exception( "event为空" );
	
//			ByteBuf bufferData 	= Unpooled.copiedBuffer(data);
			
			
//			int uname			= NettyConfig.getAttachment( ctx );
//			
//			switch( event ){
//			case PLAYERLOGINEVENT: // 是登陆游戏
//				if( uname != 0 ) throw new Exception( "已经登陆游戏" );
//				((PlayerLoginEvent)event.getEventInstance()).run( ctx, bufferData );
//				break;
//			case PLAYERREGISTEREVENT:// 是注册账号
//				if( uname != 0 ) throw new Exception( "已经登陆游戏" );
//				((PlayerRegisterEvent)event.getEventInstance()).run( ctx, bufferData );
//				break;
//			default:// 游戏其他操作
//				if( uname == 0 ) throw new Exception( "还没登陆游戏" );
//				PlayerManager.getInstance().eventRun( uname, event, bufferData );
//				break;
//			}
			
		} catch (Exception e) {
			Logs.error( "分发包错误 包号("+packageNo+") IP(" + IP.formAddress(ctx)+ "), 错误信息:" + e.getMessage() );
		}
		
	}

	//有用户退出
	public void exit( ChannelHandlerContext ctx ) {
//		int uname = NettyConfig.getAttachment( ctx );
//		if( uname == 0 ) return;
//		PlayerManager.getInstance().exit( uname );
	}
	
}
