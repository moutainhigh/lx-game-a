package cn.xgame.a.system;

import java.util.Properties;

import x.javaplus.util.Util.Propertie;

/**
 * 系统配置文件
 * @author deng		
 * @date 2015-6-11 下午2:54:09
 */
public class SystemCfg {
	
	/** 是否 调试 模式 */
	public static boolean DEBUG = true;
	
	
	/** 游戏 端口 */
	public static final int GS_PORT 	;
	
	/** 用户 端口 */
	public static final int USER_PORT 	;
	
	static {
		
		Properties properties = Propertie.loadProperty( "system.properties" );
		
		DEBUG		= Boolean.parseBoolean( properties.getProperty( "debug" ) );
		GS_PORT 	= Integer.parseInt( properties.getProperty( "gsPort" ) );
		USER_PORT 	= Integer.parseInt( properties.getProperty( "userPort" ) );
	}
	
}
