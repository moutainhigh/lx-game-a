package cn.xgame.system;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import x.javaplus.util.Resources;


/**
 * 系统配置文件
 * @author deng		
 * @date 2015-6-11 下午2:54:09
 */
public class SystemCfg {
	
	/** 是否 调试 模式 */
	public static boolean DEBUG = true;
	
	/** 开服时间*/
	public static long 			START_MILS;
	/** 配置表 路径*/
	public static String		FILE_NAME;
	
	
	/** 服务器ID */
	public static short 		ID 	;
	
	/** 服务器 名字 */
	public static String 		GS_NAME 	;
	
	/** 服务器 端口 */
	public static int 			GS_PORT 	;
	
	/** 服务器 数据库名字 */
	private static String		GS_DATABASENAME	;

	
	/** 登录服 ip地址 */
	public static String  		LS_ADDRESS 	;
	
	/** 登录服 端口 */
	public static int 			LS_PORT 	;
	
	
	static{
		try {
			SAXBuilder builder 	= new SAXBuilder();
			Document document	= builder.build( Resources.getResource( "system.xml" ) );
			Element root 		= document.getRootElement();
			
			START_MILS		= System.currentTimeMillis();
			DEBUG			= Boolean.parseBoolean( root.getChildText( "debug" ) );
			FILE_NAME		= root.getChildText( "file_name" ) ;
			
			ID				= Short.parseShort( root.getChildText( "gsId" ) );
			GS_NAME 		= root.getChildText( "gsName" ) ;
			GS_PORT 		= Integer.parseInt( root.getChildText( "gsPort" ) );
			GS_DATABASENAME	= root.getChildText( "gsDatabaseName" ) ;
			
			LS_ADDRESS 		= root.getChildText( "lsAddress" );
			LS_PORT 		= Integer.parseInt( root.getChildText( "lsPort" ) );
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * 获取该服务器的 数据库名字
	 * @return
	 */
	public static String getDatabaseName(){
		return GS_DATABASENAME+ID;
	}
	
}
