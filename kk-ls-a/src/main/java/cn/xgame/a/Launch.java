package cn.xgame.a;

import org.apache.log4j.PropertyConfigurator;

import x.javaplus.util.Resources;


import cn.xgame.net.http.server.HttpServer;
import cn.xgame.net.netty.server.NettyServer;
import cn.xgame.system.SystemCfg;

/***
 * 程序入口类
 * @author deng		
 * @date 2015-6-11 下午2:36:38
 */
public class Launch {

	public static void main(String[] args) throws Exception {
		
		// 1 加载系统配置
		initSystemProperties();
		
		// 2 启动服务器
		startServer();
		
	}
	
	
	private static void initSystemProperties() {
		
		// 初始化 log4j
		PropertyConfigurator.configureAndWatch( Resources.getResource("log4j.properties") );
		
	}
	
	private static void startServer() throws Exception {
		
		// 启动 用户登录服务器
		NettyServer userServer = new NettyServer( SystemCfg.USER_PORT );
		if( userServer.start() )
			System.out.println( "user_server starting success  port:" + SystemCfg.USER_PORT );
		
		// 启动 游戏登录服务器
		NettyServer gsServer = new NettyServer( SystemCfg.GS_PORT );
		if( gsServer.start() )
			System.out.println( "gs_server starting success  port:" + SystemCfg.GS_PORT );
		
		// 启动GM工具服务器
		new HttpServer().start( 31000 );
	}

}
