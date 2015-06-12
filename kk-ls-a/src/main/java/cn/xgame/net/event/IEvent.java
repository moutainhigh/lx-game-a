package cn.xgame.net.event;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import cn.xgame.net.netty.Netty.Config;

public abstract class IEvent {
	
	private short packageNo;
	public short getEventId (){ return packageNo; }
	public void setEventId( short eventId ){ this.packageNo = eventId; }
	
	/**
	 * 从客户端收取包并进行逻辑处理
	 * 通常也会返回一个应答包到客户端
	 * @throws IOException 
	 */
	public abstract void run( ChannelHandlerContext ctx, ByteBuf data ) throws IOException;
	
	/**
	 * 创建一个长度为capacity的缓冲包，此包包括包头，包号，包长（占位符）
	 * @param capacity
	 * @return
	 */
	protected ByteBuf buildEmptyPackage( int capacity ){
		ByteBuf buffer = Unpooled.buffer( capacity );
		buffer.writeByte( Config.PACKAGE_HEAD );
		buffer.writeByte( packageNo );
		buffer.writeShort( (short) 0 );//长度占位符
		return buffer;
	}
	/**
	 * 创建一个长度为capacity的缓冲包，此包包括包头，包号，包长（占位符）
	 * @param capacity
	 * @return
	 */
	protected ByteBuf buildEmptyPackage( ChannelHandlerContext ctx,int capacity ){
		ByteBuf buffer = ctx.alloc().buffer( capacity + 5 );
		buffer.writeByte( Config.PACKAGE_HEAD );
		buffer.writeShort( packageNo );
		buffer.writeShort( (short) 0 );//长度占位符
		return buffer;
	}
	
	/**
	 * 向客户端发送包
	 */
	public void sendPackage( ChannelHandlerContext ctx, ByteBuf buffer ) throws IOException{
		buffer.setShort( 2, (short) (buffer.writerIndex() - 5) );//设置内容长度
		buffer.writeByte( Config.PACKAGE_FOOT );
		
//		buffer.capacity( buffer.writerIndex() );
		if( ctx == null || !ctx.channel().isActive() ){
//			Logs.error( "发包时出错  con=null！ （可能原因服务端还没发包时客户端就已经断开连接了）" );
		}else{
			ctx.writeAndFlush( buffer );
		}
	}
	
}
