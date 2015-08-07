package cn.xgame.a.gs;

import cn.xgame.net.netty.Netty.Attr;
import cn.xgame.net.netty.Netty.IP;
import io.netty.channel.ChannelHandlerContext;

/**
 * 一个服务器结构
 * @author deng		
 * @date 2015-8-7 下午1:09:10
 */
public class GSData {
	
	// 服务器 ID
	private short 		id 					= 0;
	// 服务器 IP地址
	private String 		ip 					= "0.0.0.0" ;
	// 服务器 端口
	private int 		port 				= 0;
	// 服务器 名字
	private String 		gsName 				= "校长的爱";
	
	// 服务器 当前人数
	private int 		peopleNum 			= 0;
	// 服务器 在线人数
	private int 		onlinePeople 		= 0;
	
	// 通信 通道
	private ChannelHandlerContext ctx 		= null;
	
	public GSData( short gsid ) { 
		this.id = gsid; 
	}
	
	public short getId() { return id; }
	
	public String getIp() { return ip; }

	public int getPort() { return port; }
	public void setPort(int port) { this.port = port; }

	public String getName() { return gsName; }
	public void setName(String name) { this.gsName = name; }

	public int getPeopleNum() { return peopleNum; }
	public void setPeopleNum(int peopleNum) { this.peopleNum = peopleNum; }

	public int getOnlinePeople() { return onlinePeople; }
	public void setOnlinePeople(int onlinePeople) { this.onlinePeople = onlinePeople; }
	
	public ChannelHandlerContext getCtx() { return ctx; }
	public void setCtx( ChannelHandlerContext ctx ) {
		this.ctx = ctx;
		if( ctx != null ){
			Attr.setAttachment( ctx, getUID() );
			ip 				= IP.formAddress(ctx);
			peopleNum 		= 0;
			onlinePeople 	= 0;
		}
	}
	
	/**
	 * 获取服务器唯一ID 经过包装后的ID
	 * @return
	 */
	public String getUID(){ 
		return "gs:" + id;
	}
	
	/**
	 * 获取服务器状态 0.流畅 1.拥挤 2.爆满
	 * @return
	 */
	public int getGSStatus() {
		if( onlinePeople < 100 )
			return 0;
		if( onlinePeople < 300 )
			return 1;
		return 2;
	}
	
	/**
	 * 服务器是否开启
	 * @return
	 */
	public boolean isOpen() {
		return ctx != null && Attr.getAttachment(ctx).equals(getUID()) && ctx.channel().isActive();
	}
	
}
