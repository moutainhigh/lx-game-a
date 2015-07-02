package cn.xgame.a.world.planet.home;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;

/**
 * 一个在母星的玩家数据
 * @author deng		
 * @date 2015-6-29 下午7:26:22
 */
public class Child implements IBufferStream{

	// 玩家唯一ID
	private String UID;
	// 玩家属于服务器
	private short gsid;
	// 话语权
	private short privilege = 0;
	// 贡献度
	private int contribution = 0;
	// 发起数
	private int sponsors = 0;
	// 通过数
	private int passs = 0;
	
	// 是否可以发起投票
	private boolean isCanSponsorVote = false;
	
	public Child( String UID, short gsid ) {
		this.UID = UID;
		this.gsid = gsid;
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeShort( privilege );
		buf.writeInt( contribution );
		buf.writeInt( sponsors );
		buf.writeInt( passs );
	}


	public void wrapBuffer(ByteBuf buf) {
		privilege 		= buf.readShort();
		contribution 	= buf.readInt();
		sponsors 		= buf.readInt();
		passs 			= buf.readInt();
	}

	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public short getGsid() {
		return gsid;
	}
	public void setGsid(short gsid) {
		this.gsid = gsid;
	}
	public short getPrivilege() {
		return privilege;
	}
	public void setPrivilege(short privilege) {
		this.privilege = privilege;
	}
	public int getContribution() {
		return contribution;
	}
	public void setContribution(int contribution) {
		this.contribution = contribution;
	}
	public void addContribution(int contributions) {
		this.contribution += contributions;
	}
	public int getSponsors() {
		return sponsors;
	}
	public void setSponsors(int sponsors) {
		this.sponsors = sponsors;
	}
	public void addSponsors( int add ){
		sponsors += add;
	}
	public int getPasss() {
		return passs;
	}
	public void setPasss(int passs) {
		this.passs = passs;
	}
	public void addPasss(int passs) {
		this.passs += passs;
	}
	public boolean isCanSponsorVote() {
		return isCanSponsorVote;
	}
	public void setCanSponsorVote(boolean isCanSponsorVote) {
		this.isCanSponsorVote = isCanSponsorVote;
	}

	
}
