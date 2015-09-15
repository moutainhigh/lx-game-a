package cn.xgame.net.netty.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.AbstractChannel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

	private AbstractChannel channel;
	private EventLoopGroup workerGroup;

	/** 开始连接 */
	public synchronized boolean connect(String inetHost, int inetPort) {

		if (isConnect())
			return false;
		try {
			workerGroup = new NioEventLoopGroup();
			Bootstrap bootstrap = new Bootstrap();

			// NioSocketChannel is being used to create a client-side Channel.
			bootstrap.group(workerGroup).channel(NioSocketChannel.class);
			// Note that we do not use childOption() here unlike we did with
			// ServerBootstrap because the client-side SocketChannel does not
			// have a parent.
			bootstrap.handler(new NClientHandle());

			// Bind and start to accept incoming connections.
			channel = (AbstractChannel) bootstrap.connect(inetHost, inetPort)
					.sync().channel();

			return true;
		} catch (Exception e) {
		}
		return false;
	}

	/** 关闭连接 */
	public synchronized void close() {
		if (!isConnect())
			return;
		channel.close();
		workerGroup.shutdownGracefully();
		workerGroup = null;
		channel = null;
	}

	/** 是否连接 */
	public boolean isConnect() {
		return channel != null && channel.isActive();
	}

	/** 获取 通道 */
	public AbstractChannel channel() {
		return channel;
	}

	static List<Integer> ls 	= new ArrayList<Integer>();
	
	public static void main( String[] args ) throws InterruptedException, NumberFormatException, IOException{
		
		NettyClient client = new NettyClient(  );
		if( client.connect( "119.147.137.167", 8100 ) ){
//			int i = 0;
//			while( true ){
//				
//				ByteBuf buffer = Unpooled.buffer( 10 );
//				NettyUtils.writeString( buffer, "c->" + i );
//				client.channel().writeAndFlush( buffer ) ;
//				System.out.println( i );
//				i++;
//				XOSleep.sleep( 500 );
//			}
			System.out.println( "连接成功" );
		}
		
		
//		BufferedReader in 	= new BufferedReader( new FileReader( "C:/Users/Administrator/Desktop/a.txt" ) );
//		String temp			= null;
//		while ((temp = in.readLine()) != null) {
//			ls.add( Integer.parseInt( temp ) );
//		}
//		in.close();
//		
//		for(int i = 0; i < 40; i++) {
//			new Thread(){
//				
//			 public void run() {
//				NettyClient client = new NettyClient(  );
//				if( client.connect( "127.0.0.1", 8000 ) ){
//					ByteBuf buffer = Unpooled.buffer( 125 );
//					buffer.writeByte( NettyConfig.PACKAGE_HEAD );//包头
//					buffer.writeShort( 101 );//包号
//					buffer.writeShort( (short) 0 );
//					buffer.writeInt( ls.remove(0) );
//					NettyUtils.writeString( buffer, "1" );
//					buffer.setShort( 3, buffer.writerIndex() - 5 );
//					buffer.writeByte( NettyConfig.PACKAGE_FOOT );
//					client.channel().writeAndFlush(buffer);
//				}
//			 }
//			}.start();
//		}
//		while( !ls.isEmpty() ){
//			
//			NettyClient client = new NettyClient(  );
//			if( client.connect( "127.0.0.1", 8000 ) ){
//				ByteBuf buffer = Unpooled.buffer( 125 );
//				buffer.writeByte( NettyConfig.PACKAGE_HEAD );//包头
//				buffer.writeShort( 101 );//包号
//				buffer.writeShort( (short) 0 );
//				buffer.writeInt( ls.remove(0) );
//				NettyUtils.writeString( buffer, "1" );
//				buffer.setShort( 3, buffer.writerIndex() - 5 );
//				buffer.writeByte( NettyConfig.PACKAGE_FOOT );
//				client.channel().writeAndFlush(buffer);
//			}
//		}
		
		
//		System.out.println( client.isConnect() );
//		client.close();
//		System.out.println( client.isConnect() );
//		client.connect( "127.0.0.1", 8080 );
//		System.out.println( client.isConnect() );
		
//		while (true){
//			
////			NByteBuf buffer = NByteBuf.allocate( 11 );
//			ByteBuf buffer = Unpooled.buffer();
//			
//			buffer.writeByte( NettyConfig.PACKAGE_HEAD );//包头
//			buffer.writeShort( 101 );//包号
//			buffer.writeShort( 1 );
//			buffer.writeByte( 0 );
//			buffer.writeByte( NettyConfig.PACKAGE_FOOT );
//			
//			if( client.isConnect() )
//				client.channel().writeAndFlush( buffer ) ;
//			
//			XOSleep.sleep( 5000 );
//        }
	}
}
