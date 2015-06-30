package cn.xgame.a.world.planet.home;

/**
 * 一个在母星的玩家数据
 * @author deng		
 * @date 2015-6-29 下午7:26:22
 */
public class Child {

	// 玩家唯一ID
	private String UID;
	// 玩家属于服务器
	private short gsid;
	// 话语权
	private byte privilege = 0;
	
	public Child( String UID, short gsid ) {
		this.UID = UID;
		this.gsid = gsid;
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
	public byte getPrivilege() {
		return privilege;
	}
	public void setPrivilege(byte privilege) {
		this.privilege = privilege;
	}
	
}
