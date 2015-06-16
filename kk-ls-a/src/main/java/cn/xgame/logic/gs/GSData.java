package cn.xgame.logic.gs;

import cn.xgame.net.netty.Netty.Attr;
import cn.xgame.net.netty.Netty.IP;
import io.netty.channel.ChannelHandlerContext;

public class GSData {
	
	// 服务器 ID
	private short id = 0;
	// 服务器 IP地址
	private String ip = "0.0.0.0" ;
	// 服务器 端口
	private int port = 0;
	// 服务器 名字
	private String name = "校长的爱";
	
	// 服务器 当前人数
	private int peopleNum = 0;
	// 服务器 状态 0.正常开启 1.关闭 2.闲置
	private GSStatus status = GSStatus.CLOSE;
	
	// 通信 通道
	private ChannelHandlerContext ctx = null;
	
	public GSData( short gsid ) {
		this.id = gsid;
	}
	public String getUID(){
		return "gs:" + id;
	}
	public short getId() {
		return id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(int peopleNum) {
		this.peopleNum = peopleNum;
	}

	public GSStatus getStatus() {
		return status;
	}

	public void setStatus(GSStatus status) {
		this.status = status;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx( ChannelHandlerContext ctx ) {
		this.ctx = ctx;
		
		if( ctx == null ){
			
			setStatus( GSStatus.CLOSE );
		}else{
			
			setIp( IP.formAddress(ctx) );
			Attr.setAttachment( ctx, getUID() );
			setStatus( GSStatus.OPEN );
		}
	}
	
	/**
	 * 服务器状态 0.流畅 1.拥挤 2.爆满
	 * @return
	 */
	public int getCurrentStatus() {
		if( peopleNum < 100 )
			return 0;
		if( peopleNum < 300 )
			return 1;
		return 2;
	}
	
	
}
