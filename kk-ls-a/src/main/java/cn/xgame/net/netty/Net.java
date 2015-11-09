package cn.xgame.net.netty;

import cn.xgame.a.gs.GSManager;
import cn.xgame.net.event.Events;
import cn.xgame.net.netty.classes.Netty.IP;
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
			
			event.run( ctx, bufferData );
			
		} catch (Exception e) {
			Logs.error( "分发包错误 包号("+packageNo+") IP(" + IP.formAddress(ctx)+ "), 错误信息:" + e.getMessage() );
		}
		
	}

	//有用户退出
	public void disconnect( ChannelHandlerContext ctx ) {
		
		GSManager.o.disconnect( ctx );
		
	}
	
	
}
