package cn.xgame.net.netty.server;

import cn.xgame.net.Net;
import cn.xgame.net.netty.Netty.Config;
import cn.xgame.net.netty.Netty.IP;
import cn.xgame.utils.Logs;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NServerHandler extends ChannelInboundHandlerAdapter{
	
	private final Net net = new Net();
	
	/**  读取数据
	 * 	byte 	包头
	 * 	short 	包号
	 *  short	包长 只包括内容长  
	 *  byte[]	内容
	 *  byte	包尾
	 *  */
	public void channelRead( ChannelHandlerContext ctx, Object msg ) {
		
		ByteBuf in 			= (ByteBuf) msg;
//		Logs.debug( "in=" + in );
		try {
			while ( in.isReadable() ) {
				
				byte head 			= in.readByte(); //包头
				short packageNo 	= in.readShort(); //包号
				short dataLength 	= in.readShort();//包长 只包括内容长//
				if ( dataLength < 0 || dataLength > Config.PACKAGE_LENGTH )
	               throw new Exception( "包长错误" );
			
				byte data[] 		= new byte[ dataLength ];
				in.readBytes( data ); //内容
				byte foot 			= in.readByte(); //包尾
				if ( !checkInputData(head, foot) )
					throw new Exception( "包头包尾错误" );

				//开始分发包
				net.packageRun( ctx, packageNo, data );
			}
		} catch (Exception e) {
			Logs.error( " 通信错误 IP(" + IP.formAddress(ctx)+ "), 错误信息:" + e.getMessage() );
			ctx.close();
		} finally {
			in.release(); // 记得要释放掉
		}
	}

	/** 有用户连*/
	public void channelActive( ChannelHandlerContext ctx ) { // (1)
		Logs.debug( IP.formAddress(ctx) + " connect!" );
	}
	
	/** 连接断开 */
	public void channelInactive( ChannelHandlerContext ctx ){
		Logs.debug( IP.formAddress( ctx ) + " disconnect!" );
		net.disconnect( ctx );
	}
	
	/** 异常 */
	public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) { // (4)
		// Close the connection when an exception is raised.
//		Logs.debug( NettyUtils.ctxToAddress( ctx ) + "  异常" );
//		ctx.close();
	}
	
	private boolean checkInputData( byte head, byte foot ) { 
		return head == Config.PACKAGE_HEAD && foot == Config.PACKAGE_FOOT; 
	}
	
}
