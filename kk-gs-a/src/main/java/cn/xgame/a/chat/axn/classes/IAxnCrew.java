package cn.xgame.a.chat.axn.classes;

import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.netty.Netty.RW;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 一个频道的玩家 结构
 * @author deng		
 * @date 2015-8-2 下午12:55:24
 */
public class IAxnCrew implements ITransformStream{

	// 玩家唯一ID
	private String 	uid;
	// 玩家名字
	private String 	name;
	// 玩家头像
	private int 	headIco;
	
	// 玩家socket
	private ChannelHandlerContext socket = null;
	

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		RW.writeString( buffer, uid );
		RW.writeString( buffer, name );
		buffer.writeInt( headIco );
	}
	
	/**
	 * 是否在线 随便刷新一下 玩家socket
	 * @return
	 */
	public boolean isOnlineAndUpdate() {
		if( socket == null || socket.channel().isActive() ){
			Player player = PlayerManager.o.getPlayerFmOnline(uid);
			if( player != null ){
				socket = player.getCtx();
			}
		}
		return socket != null && socket.channel().isActive();
	}
	
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public ChannelHandlerContext getSocket() {
		return socket;
	}
	public void setSocket(ChannelHandlerContext socket) {
		this.socket = socket;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHeadIco() {
		return headIco;
	}
	public void setHeadIco(int headIco) {
		this.headIco = headIco;
	}



	
}
