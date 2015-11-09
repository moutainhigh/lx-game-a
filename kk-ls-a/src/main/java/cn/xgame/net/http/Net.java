package cn.xgame.net.http;

import net.sf.json.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import cn.xgame.net.event.all.gm.Events;
import cn.xgame.net.netty.classes.Netty.IP;
import cn.xgame.utils.Logs;

public class Net {

	public void packageRun(ChannelHandlerContext ctx, int packageNo, JSONObject data, HttpRequest request) {
		
		Events event 		= Events.fromNum( packageNo );
		
		try{
			if( event == null ) throw new Exception( "event为空" );
	
			event.run( ctx, data, request );
			
		} catch (Exception e) {
			Logs.error( "分发包错误 包号("+packageNo+") IP(" + IP.formAddress(ctx)+ "), 错误信息:" + e.getMessage() );
		}
	}
	
}
