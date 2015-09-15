package cn.xgame.a.gs;

import java.util.List;

import cn.xgame.net.netty.Netty.Attr;


import io.netty.channel.ChannelHandlerContext;
import x.javaplus.collections.Lists;
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
	private List<GSData> gss 	= Lists.newArrayList();
	
	// 推荐服务器ID
	private short recommendGsid	= -1;
	
	
	
	/**
	 * 获取开启 服务器 列表
	 * @return
	 */
	public List<GSData> getOpenGs(){
		List<GSData> ret = Lists.newArrayList();
		for( GSData gs : gss ){
			if( gs.isOpen() ){
				ret.add(gs);
			}
		}
		return ret;
	}
	
	/**
	 * 服务器 连接
	 * @param gsid
	 * @param name
	 * @param ip 
	 * @param port
	 * @param ctx
	 * @return
	 */
	public ErrorCode connect( short gsid, String name, String ip, int port, ChannelHandlerContext ctx ) {
		
		GSData gs = getGs( gsid );
		
		if( gs == null ){
			
			gs = new GSData( gsid );
			gss.add( gs );
			
		}else if( gs.isOpen() ){
			
//			return ErrorCode.GS_EXIST;
		}
		
		gs.setCtx( ctx );
		gs.setName( name );
		gs.setPort( port );
		gs.setIp( ip );
		
		return ErrorCode.SUCCEED;
	}

	/**
	 * 获取服务器
	 * @param gsid
	 * @return
	 */
	public GSData getGs( short gsid ) {
		for( GSData gs : gss ){
			if( gs.getId() == gsid ){
				return gs;
			}
		}
		return null;
	}


	/**
	 * 有服务器 断开 
	 * @param ctx
	 */
	public void disconnect( ChannelHandlerContext ctx ) {
		
		short id 	= getGsid( ctx );
		GSData gs 	= getGs( id );
		if( gs == null )
			return ;
		// 直接设置为null
		gs.setCtx( null );
	}
	
	private short getGsid( ChannelHandlerContext ctx ){
		String attr = Attr.getAttachment(ctx);
		return attr == null ? -1 : Short.parseShort( attr.replaceAll( "gs:", "") );
	}

	/**
	 * 更新 服务器 人数
	 * @param gsid
	 * @param peopleNum
	 */
	public void updatePeople( short gsid, int peopleNum, int onlinePeople ) {
		
		GSData gs = getGs( gsid );
		if( gs == null )
			return;
		
		gs.setPeopleNum( peopleNum );
		gs.setOnlinePeople( onlinePeople );
		
		// 更新推荐服务器ID
		updateRecommendGsid();
	}

	// 更新推荐服务器ID
	private void updateRecommendGsid() {
		int num = Integer.MAX_VALUE;
		for( GSData gs : gss ){
			if( !gs.isOpen() || gs.getPeopleNum() > num )
				continue;
			num 			= gs.getPeopleNum();
			recommendGsid 	= gs.getId();
		}
	}

	/** 获取推荐服务器id */
	public short getRecommendGsid() {
		return recommendGsid;
	}
	public void setRecommendGsid(short recommendGsid) {
		this.recommendGsid = recommendGsid;
	}
	
}
