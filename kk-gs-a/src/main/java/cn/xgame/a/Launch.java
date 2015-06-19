package cn.xgame.a;

import io.netty.channel.AbstractChannel;

import org.apache.log4j.PropertyConfigurator;

import cn.xgame.a.system.SystemCfg;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.ls.ConnectEvent;
import cn.xgame.net.netty.client.NettyClient;
import cn.xgame.net.netty.server.NettyServer;
import cn.xgame.utils.Logs;
import cn.xgame.utils.runnable.ThreadManager;

import x.javaplus.ip.IPSeeker;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.Resources;




/***
 * 程序入口类
 * @author deng		
 * @date 2015-6-11 下午2:36:38
 */
public class Launch {

	
	public static class LSClientAgency{
		
		private static NettyClient client;

		public static boolean connect( String ip, int port ) {
			client = new NettyClient();
			return client.connect( ip, port );
		}

		public static AbstractChannel socket() {
			return client.channel();
		}
		
		public static void close(){
			client.close();
			client = null;
		}
		
	}
	
	public static void main(String[] args) {
		
		try {
			
			// 1 加载系统配置
			initSystemProperties();
			
			// 2 连接 and 登录 - 登录服务器 
			connectLoginServer();
			
		} catch (Exception e) {
			Logs.error( e.getMessage() );
			LSClientAgency.close();
		}
		
	}
	
	/**  连接登录服务 响应  */
	public static void handleConnect( ErrorCode code ){
		
		try {
			if( code != ErrorCode.SUCCEED )
				throw new Exception( code.getDesc() );
			
			Logs.debug( "登录服 登录成功" );
			
			// 启动游戏服务器
			startServer();
			
			// 启动线程
			ThreadManager.start();
			
			
			Logs.debug( "游戏服 启动成功" );
			
		} catch (Exception e) {
			Logs.error( e.getMessage() );
			LSClientAgency.close();
		}
			
	}
	
	private static void connectLoginServer() throws Exception {
		if( !LSClientAgency.connect( SystemCfg.LS_ADDRESS, SystemCfg.LS_PORT ) )
			throw new Exception( "登录服 连接失败" );
		Logs.debug( "登录服 连接成功" );
		
		// 这里开始 登录 登录服务器
		((ConnectEvent)Events.GS_CONNECT.getEventInstance()).run(  );
	}

	private static void initSystemProperties() {
		
		// 初始化 log4j
		PropertyConfigurator.configureAndWatch( Resources.getResource("log4j.properties") );
		
		// 初始化 ip数据库
		IPSeeker.I.init( "qqwry.dat" );
		
		// 加载配置表
		CsvGen.load();
	}
	
	private static void startServer() {
		
		// 启动 服务器
		new NettyServer( SystemCfg.GS_PORT );
		
	}

}
