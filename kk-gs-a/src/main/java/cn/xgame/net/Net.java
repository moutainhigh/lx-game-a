package cn.xgame.net;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.pl.CreateEvent;
import cn.xgame.net.event.all.pl.LoginEvent;
import cn.xgame.net.netty.Netty.Attr;
import cn.xgame.net.netty.Netty.IP;
import cn.xgame.utils.Logs;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class Net {
	
	
	public void packageRun( ChannelHandlerContext ctx, short packageNo, byte[] data) {
		
		Events event 		= Events.fromNum( packageNo );
		
		Logs.debug( ctx, "申请包 " + packageNo );
		try{
			if( event == null ) throw new Exception( "event为空" );
	
			ByteBuf bufferData 	= Unpooled.copiedBuffer(data);
			
			String UID			= Attr.getAttachment( ctx );
			
			switch( event ){
			case PLAYER_LOGIN: // 是登陆游戏
				if( UID != null ) throw new Exception( "已经登陆游戏" );
				((LoginEvent)event.getEventInstance()).run( ctx, bufferData );
				break;
			case PLAYER_CREATE:// 是创建角色
				if( UID != null ) throw new Exception( "已经登陆游戏" );
				((CreateEvent)event.getEventInstance()).run( ctx, bufferData );
				break;
			default:// 游戏其他操作
				if( UID == null ) throw new Exception( "还没登陆游戏" );
				PlayerManager.o.eventRun( UID, event, bufferData );
				break;
			}
				
		} catch (Exception e) {
			Logs.error( "分发包错误 包号("+packageNo+") IP(" + IP.formAddress(ctx)+ ") " + e.getMessage() );
		}
		
	}

	//有用户退出
	public void disconnect( ChannelHandlerContext ctx ) {
		String UID	= Attr.getAttachment( ctx );
		if( UID == null ) return;
		PlayerManager.o.exit( UID );
	}
	
	
}
