package cn.xgame.net.http.classes;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpHeaders.Values;

import java.io.IOException;



import net.sf.json.JSONObject;

public abstract class IEvent {
	private int packageNo;
	public int getEventId (){ return packageNo; }
	public void setEventId( int eventId ){ this.packageNo = eventId; }
	
	/**
	 * 向客户端发送包
	 */
	public void sendPackage( ChannelHandlerContext ctx, JSONObject result, HttpRequest request ) throws IOException{
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(result.toString().getBytes("UTF-8")));
        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        if (HttpHeaders.isKeepAlive(request)) {
            response.headers().set(CONNECTION, Values.KEEP_ALIVE);
        }
        ctx.write(response);
        ctx.flush();
	}
	
	/**
	 * 从客户端收取包并进行逻辑处理
	 * 通常也会返回一个应答包到客户端
	 * @throws IOException 
	 */
	public abstract void run(ChannelHandlerContext ctx, JSONObject data, HttpRequest request) throws IOException;
}
