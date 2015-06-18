package cn.xgame.gen.o;


/**
 * 玩家数据
 * @author deng		
 * @date 2015-6-15 下午4:33:36
 */
public class PlayerData {
	
	/** 唯一ID */
	String uid;

	/** 服务器ID */
	short gsid;
	
	/** 名字 */
	String nickname;
	
	/** 头像图标ID */
	int headIco;
	
	/** 区域 */
	String country;
	
	/** 游戏币 */
	int currency;
	
	/** 充值币 */
	int gold;
	
	/** 创建时间 */
	long createTime;
	
	/** 上次下线时间 */
	long lastLogoutTime;
	
	/** 背包 */
	byte[] bags;
	
}
