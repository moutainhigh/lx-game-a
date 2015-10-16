package cn.xgame.net.netty.client;


import cn.xgame.net.event.Events;
import cn.xgame.net.netty.Netty.Config;
import cn.xgame.net.netty.Netty.IP;
import cn.xgame.utils.Logs;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NClientHandle extends ChannelInboundHandlerAdapter {

	public void channelRead( ChannelHandlerContext ctx, Object msg ) {
		
		short packageNo 	= 0;
		short dataLength 	= 0;
		ByteBuf in 			= (ByteBuf) msg;
//		System.out.println( "in=" + in );
		try {
			while ( in.isReadable() ) {
				
				byte head 	= in.readByte();
				packageNo 	= in.readShort(); 
				dataLength 	= in.readShort();
				if ( dataLength < 0 || dataLength > Config.PACKAGE_LENGTH )
	               throw new Exception( "包长错误" );

				byte data[] = new byte[ dataLength ];
				in.readBytes( data );
				byte foot 	= in.readByte();

				if ( !checkInputData(head, foot) ) //
					throw new Exception( "包头包尾错误" );

				Events event = Events.fromNumber( packageNo );
				if( event == null ) throw new Exception( "event为空" );
				ByteBuf bufferData 	= Unpooled.copiedBuffer(data);
					
				event.run( null, bufferData );
			}
			
		} catch (Exception e) {
			Logs.error( "分发包错误 包号("+packageNo+") IP(" + IP.formAddress(ctx)+ "), 错误信息:" + e.getMessage() );
		} finally {
			in.release(); // 最后要记得释放掉
		}
	}
	
	/** 有用户连接 */
	public void channelActive( ChannelHandlerContext ctx ) { // (1)
//		System.out.println( buildPrefixString( ctx ) + " 连接" );
	}
	
	/** 连接断开 */
	public void channelInactive( ChannelHandlerContext ctx ){
//		System.out.println( buildPrefixString( ctx ) + " 退出" );
	}
	
	/** 异常 */
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
//		cause.printStackTrace();
//		ctx.close();
	}
	
	
	private boolean checkInputData( byte head, byte foot ) {
		return head == Config.PACKAGE_HEAD && foot == Config.PACKAGE_FOOT;
	}
}
