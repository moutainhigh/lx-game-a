package cn.xgame.net.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer{

	private EventLoopGroup boss;
	private EventLoopGroup worker;
	
    private int port;
    
    public NettyServer( int port ) {
        this.port 	= port;
    }
    
    public synchronized boolean start(){
    	if( isRunning() ) 
    		return false;
    	try {
	    	boss 		= new NioEventLoopGroup(  );
	        worker 		= new NioEventLoopGroup(  );
	        ServerBootstrap bootstrap = new ServerBootstrap();
	    	bootstrap.group( boss, worker ).channel( NioServerSocketChannel.class ).childHandler( new ChannelInitializer<SocketChannel>() { // (4)
	  			public void initChannel(SocketChannel ch) throws Exception {
	  				ch.pipeline().addLast( new NServerHandler() );
	  			}
	    	}).childOption( ChannelOption.SO_KEEPALIVE, false );
	    	
	    	// Bind and start to accept incoming connections.
			bootstrap.bind(port).sync();
		} catch (InterruptedException e) { e.printStackTrace(); }
    	return true;
    }
    
	public synchronized void stop(){
		if( !isRunning() ) return;
		boss.shutdownGracefully();
		worker.shutdownGracefully();
    	worker 		= null;
    	boss 		= null;
    }
    
    public boolean isRunning(){ return worker != null && boss != null; }

}
