package cn.xgame.a.world.planet.data.vote;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.net.netty.Netty.RW;

/**
 * 一个投票的玩家数据
 * @author deng		
 * @date 2015-7-2 上午11:24:35
 */
public class VotePlayer {

	private String UID;
	
	// 记录时候 的话语权
	private short privilege;

	public VotePlayer( Child player ) {
		this.UID = player.getUID();
		this.privilege = player.getPrivilege();
	}
	
	/**
	 * 从数据库获取
	 * @param buf
	 */
	public VotePlayer( ByteBuf buf ){
		UID = RW.readString(buf);
		privilege = buf.readShort();
	}
	
	public void putBuffer( ByteBuf buf ) {
		RW.writeString(buf, UID);
		buf.writeShort(privilege);
	}
	
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public short getPrivilege() {
		return privilege;
	}
	public void setPrivilege(short privilege) {
		this.privilege = privilege;
	}



}
