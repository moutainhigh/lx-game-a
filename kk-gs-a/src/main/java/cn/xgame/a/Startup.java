package cn.xgame.a;

import java.io.File;

import io.netty.channel.AbstractChannel;

import org.apache.log4j.PropertyConfigurator;

import cn.xgame.a.chat.axn.classes.ChatAxnUID;
import cn.xgame.a.world.WorldManager;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.ls.ConnectEvent;
import cn.xgame.net.netty.client.NettyClient;
import cn.xgame.net.netty.server.NettyServer;
import cn.xgame.system.LXConstants;
import cn.xgame.system.SystemCfg;
import cn.xgame.utils.Logs;
import cn.xgame.utils.runnable.ThreadManager;

import x.javaplus.ip.IPSeeker;
import x.javaplus.mysql.App;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.Resources;
import x.javaplus.util.Util.Time;
import x.javaplus.util.lua.Lua;




/***
 * 程序入口类
 * @author deng		
 * @date 2015-6-11 下午2:36:38
 */
public class Startup {

	/** 一个简单的 客户端 */
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
			Time.beginTimer();
			
			// 0.构建数据库
			buildDB( "sql" );
			
			// 1.加载系统配置
			initSystemProperties();
			
			// 2.连接 and 登录 - 登录服务器 
			connectLoginServer();
			
		} catch (Exception e) {
			Logs.error( "Launch.main" + e.getMessage() );
			LSClientAgency.close();
		}
		
	}

	/**  连接登录服务 响应  */
	public static void handleConnect( ErrorCode code ){
		
		try {
			if( code != ErrorCode.SUCCEED )
				throw new Exception( code.getDesc() );
			
			Logs.debug( SystemCfg.LS_ADDRESS+":"+SystemCfg.LS_PORT+" 登录服 登录成功" );
			
			// 1.加载配置表
			CsvGen.load();
			Logs.debug( "配置表加载完成" );
			
			// 2.初始化 星图
			WorldManager.o.initialize();
			Logs.debug( "星图初始化完成" );
			
			// 3.启动游戏服务器
			new NettyServer( SystemCfg.GS_PORT ).start();
			
			// 4.启动线程
			ThreadManager.start();
			Logs.debug( "Server startup in " + Time.toEndTime() + " ms!" );
			
		} catch (Exception e) {
			Logs.error( "Launch.handleConnect", e );
			LSClientAgency.close();
		}
		
	}
	
	private static void connectLoginServer() throws Exception {
		if( !LSClientAgency.connect( SystemCfg.LS_ADDRESS, SystemCfg.LS_PORT ) )
			throw new Exception( "登录服 连接失败" );
		Logs.debug( "登录服 连接成功" );
		
		// 这里开始 登录 登录服务器
		((ConnectEvent)Events.GS_CONNECT.toInstance()).run(  );
	}

	private static void initSystemProperties() {
		
		// 初始化 log4j
		PropertyConfigurator.configureAndWatch( Resources.getResource("log4j.properties") );
		
		// 初始化 ip数据库
		IPSeeker.I.init( Resources.getResource( "qqwry.dat" ) );
		
		// lua日志初始
		Lua.setLogClass( Logs.class );
		
		// 加载游戏基础配置文件
		LXConstants.load();
		
		// 读取游戏存储的二进制文件
		ChatAxnUID.readFile();
		
		Logs.debug( "系统配置加载完成" );
	}
	
	// 构建数据库
	private static void buildDB( String path ) {
		if( new File( path ).isDirectory() )
			App.generateMysql( SystemCfg.getDatabaseName(), path );
	}
	

}
