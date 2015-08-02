package cn.xgame.a.chat.o;

import x.javaplus.util.ErrorCode;
import cn.xgame.system.LXConstants;

/**
 * 频道ID管理中心
 * @author deng		
 * @date 2015-8-2 下午2:57:34
 */
public class ChatAxnUID {

	// 临时频道唯一ID
	private static int TEMPAXN_UID = 0;
	
	// 组队频道唯一ID
	private static int TEAMAXN_UID = 0;
	
	/** 
	 * 获取临时频道唯一ID  s
	 * @throws Exception 
	 * */
	public static int getTempaxnUid() throws Exception{ 
		if( ++TEMPAXN_UID >= LXConstants.CHAT_UID )
			throw new Exception( ErrorCode.OTHER_ERROR.name() );
		return TEMPAXN_UID;
	}
	/** 获取组队频道唯一ID  */
	public static int getTeamaxnUid() throws Exception{ 
		if( ++TEMPAXN_UID >= LXConstants.CHAT_UID )
			throw new Exception( ErrorCode.OTHER_ERROR.name() );
		return TEAMAXN_UID; 
	}
	
	
}
