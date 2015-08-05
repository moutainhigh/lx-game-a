package cn.xgame.a.chat.o;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import cn.xgame.system.LXConstants;

/**
 * 频道ID管理中心
 * @author deng		
 * @date 2015-8-2 下午2:57:34
 */
public class ChatAxnUID {

	// 临时频道唯一ID
	private static int 				TEMP_AXNUID = 0;
	// 临时存放UID 方便重复利用
	private static List<Integer> 	TEMP_UIDLS 	= Lists.newArrayList();
	
	// 组队频道唯一ID
	private static int 				TEAM_AXNUID = 0;
	// 临时存放UID 方便重复利用
	private static List<Integer> 	TEAM_UIDLS 	= Lists.newArrayList();
	
	
	
	/** 
	 * 获取临时频道唯一ID
	 * @throws Exception 
	 * */
	public static int getTempaxnUid() throws Exception{
		if( !TEMP_UIDLS.isEmpty() )
			return TEMP_UIDLS.remove(0);
		if( ++TEMP_AXNUID >= LXConstants.CHAT_UID )
			throw new Exception( ErrorCode.OTHER_ERROR.name() );
		return TEMP_AXNUID;
	}
	/** 获取组队频道唯一ID  */
	public static int getTeamaxnUid() throws Exception{
		if( !TEAM_UIDLS.isEmpty() )
			return TEAM_UIDLS.remove(0);
		if( ++TEMP_AXNUID >= LXConstants.CHAT_UID )
			throw new Exception( ErrorCode.OTHER_ERROR.name() );
		return TEAM_AXNUID; 
	}
	
	/**
	 * 添加一个临时频道UID
	 * @param id
	 */
	public static void appendTempUid( int id ){
		if( TEMP_UIDLS.indexOf(id) == -1 )
			TEMP_UIDLS.add( id );
	}
	
	/**
	 * 添加一个组队频道UID
	 * @param id
	 */
	public static void appendTeamUid( int id ){
		if( TEAM_UIDLS.indexOf(id) == -1 )
			TEAM_UIDLS.add( id );
	}
	
	/** 读取文件 */
	public static void readFile(){
		TEMP_AXNUID = 0;
		TEMP_UIDLS.clear();
		TEAM_AXNUID = 0;
		TEAM_UIDLS.clear();
	}
	
}
