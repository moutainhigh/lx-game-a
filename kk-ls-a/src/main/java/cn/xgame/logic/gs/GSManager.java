package cn.xgame.logic.gs;

import java.util.HashMap;
import java.util.Map;


import io.netty.channel.ChannelHandlerContext;
import x.javaplus.util.ErrorCode;

/**
 * 游戏服务器 管理中心
 * @author deng		
 * @date 2015-6-12 下午4:25:17
 */
public class GSManager {

	public static GSManager o = new GSManager();
	private GSManager(){}
	
	
	// 服务器 列表
	private Map<Short, GSData> gss = new HashMap<Short, GSData>();
	
	
	/**
	 * 服务器 连接
	 * @param gsid
	 * @param name
	 * @param port
	 * @param ctx
	 * @return
	 */
	public ErrorCode connect( short gsid, String name, int port, ChannelHandlerContext ctx ) {
		
		GSData gs = get( gsid );
		
		if( gs == null ){
			gs = new GSData( gsid );
			gss.put( gs.getId(), gs );
		}else{
			
			if( !gs.ctxIsNull() )
				return ErrorCode.GS_EXIST;
		}
		
		gs.setStatus( GSStatus.OPEN );
		gs.setName( name );
		gs.setCtx( ctx );
		gs.setPort( port );
		
		return ErrorCode.SUCCEED;
	}

	
	public GSData get( short gsid ) {
		return gss.get(gsid);
	}
	
	
	
}
