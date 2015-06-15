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
	
	
	/** 服务器ID */
	public static short 		ID 	;
	
	/** 服务器 名字 */
	public static String 		GS_NAME 	;
	
	/** 服务器 端口 */
	public static int 			GS_PORT 	;
	
	

	/** 登录服 ip地址 */
	public static String  		LS_ADDRESS 	;
	
	/** 登录服 端口 */
	public static int 			LS_PORT 	;
	
	
	
	static {
		
		Properties properties = Propertie.loadProperty( "system.properties" );
		
		DEBUG		= Boolean.parseBoolean( properties.getProperty( "debug" ) );
		
		ID			= Short.parseShort( properties.getProperty( "gsId" ) );
		GS_NAME		= properties.getProperty( "gsName" );
		GS_PORT 	= Integer.parseInt( properties.getProperty( "gsPort" ) );
		
		LS_ADDRESS	= properties.getProperty( "lsAddress" );
		LS_PORT		= Integer.parseInt( properties.getProperty( "lsPort" ) );
	}
	
//	public static void init(){
//		try {
//			SAXBuilder builder 	= new SAXBuilder();
//			Document document	= builder.build( FILE );
//			Element root 		= document.getRootElement();
//			
//			START_MILS		= XOTime.currentTimeMillis();
//			IS_DEBUG		= Boolean.parseBoolean( root.getChildText( "is_debug" ) );
//			FILE_NAME		= root.getChildText( "file_name" );
//			GAME_DISTRICT 	= Byte.parseByte( root.getChildText( "game_district" ) );
//			PORT 			= Integer.parseInt( root.getChildText( "port" ) );
//			LOGIN_ADDRESS 	= root.getChildText( "login_address" );
//			LOGIN_PORT 		= Integer.parseInt( root.getChildText( "login_port" ) );
//			WORLD_ADDRESS	= root.getChildText( "world_address" );
//			LOG_PATH		= root.getChildText( "log_path" );
//			PLATFORM		= root.getChildText( "platform" );
//			SERVER_NAME		= root.getChildText( "server_name" );
//			SERVER_ADDRESS	= root.getChildText( "server_address" );
//			String content 	= root.getChildText( "combine_district" );
//			if( !content.isEmpty() ){
//				String arr[] 		= content.split( "," );
//				COMBINE_DISTRICT 	= new byte[arr.length];
//				for( int i = 0; i < arr.length; i++ ){
//					COMBINE_DISTRICT[i] = Byte.parseByte( arr[i] );
//				}
//			}
//			
//			
//		} catch (JDOMException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//	}	
}
