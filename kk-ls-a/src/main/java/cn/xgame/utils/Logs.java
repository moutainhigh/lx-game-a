package cn.xgame.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.xgame.a.system.SystemCfg;
import cn.xgame.logic.user.UserData;

/**
 * 日志类 包括控制台输出
 * @author deng		
 * @date 2015-6-11 下午2:58:15
 */
public class Logs {
	
	private static final Logger	logger 	= Logger.getRootLogger();
	
	/**
	 * 错误 日志*/
	public static void error( Object log ) { logger.error( "[ systen ]: "+log ); }
	public static void error( Object log, Exception e ) { logger.error( "[ systen ]: "+log, e ); }
	public static void error( UserData player, Object log ) { logger.error( buildPrefixStr( player ) + log ); }
	public static void error( UserData player, Object log, Exception e ) { logger.error( buildPrefixStr( player ) + log , e ); }
	
	/** debug输出 */
	public static void debug( Object log ) { if( SystemCfg.DEBUG ) logger.debug( "[ systen ]: "+log ); }
	public static void debug( UserData player, Object log ) { if( SystemCfg.DEBUG ) logger.debug( buildPrefixStr( player ) + log ); }
	
	//针对此类 做一个前缀包装
	private static String buildPrefixStr( UserData player ) {
		if( player == null ) return "[ null ]: ";
		return "[ " + player.getUID() + " ]: ";
	}
	
	public static void main(String[] args) {
		PropertyConfigurator.configureAndWatch( "log4jconfig/log4j.properties" );
		
		error( "asdasds" );
	}
}
