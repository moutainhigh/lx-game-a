package cn.xgame.net.http.server;

import cn.xgame.net.http.Net;
import cn.xgame.utils.Logs;
import net.sf.json.JSONObject;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

/**
 * 响应请求的HttpServerInboundHandler：
 * @author deng		
 * @date 2015-9-15 上午5:00:28
 */
public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {
	
	private final Net net = new Net();
	
    private HttpRequest request;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;

//            String uri = request.getUri();
//            Logs.debug( "有人连接 url=" + uri );
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf 	= content.content();
            JSONObject data = JSONObject.fromObject( buf.toString(CharsetUtil.UTF_8) );
            buf.release();
            Logs.debug( data.toString() );
            
            net.packageRun( ctx, data.getInt("protocolNo"), data.getJSONObject("data"), request );
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}