package cn.xgame.a.player;

/**
 * 所有玩家对象 基类
 * @author deng		
 * @date 2015-7-9 下午3:02:06
 */
public class IUObject {

	// 唯一ID
	private int uId;
	// 表格ID
	private int nId;
	
	
	public IUObject( int uid, int nid ){
		this.uId = uid;
		this.nId = nid;
	}
	
	public int getuId() {
		return uId;
	}
	public void setuId(int uId) {
		this.uId = uId;
	}
	public int getnId() {
		return nId;
	}
	public void setnId(int nId) {
		this.nId = nId;
	}
	
	
}
