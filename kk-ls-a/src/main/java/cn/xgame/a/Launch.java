package cn.xgame.a;

import org.apache.log4j.PropertyConfigurator;

import x.javaplus.ip.IPSeeker;
import x.javaplus.util.Resources;


import cn.xgame.net.netty.server.NettyServer;
import cn.xgame.system.SystemCfg;

/***
 * 程序入口类
 * @author deng		
 * @date 2015-6-11 下午2:36:38
 */
public class Launch {

	public static void main(String[] args) {
		
		// 1 加载系统配置
		initSystemProperties();
		
		// 2 启动服务器
		startServer();
		
	}
	
	
	private static void initSystemProperties() {
		
		// 初始化 log4j
		PropertyConfigurator.configure( Resources.getResource("log4j.properties") );
		
		// 初始化 ip数据库
		IPSeeker.I.init( "qqwry.dat" );
		
	}
	
	private static void startServer() {
		// 启动 游戏服务器
		new NettyServer( SystemCfg.PORT ).start();
	}

}
