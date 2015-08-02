package cn.xgame.gen.o;


/**
 * 玩家数据
 * @author deng		
 * @date 2015-6-15 下午4:33:36
 */
public interface PlayerData {
	
	/** 服务器ID */
	short gsid();
	
	/** 唯一ID */
	String uid();
	
	/** 创建时间 */
	long createTime();
	
	/** 上次下线时间 */
	long lastLogoutTime();
	
	/** 名字 */
	String nickname();
	
	/** 头像图标ID */
	int headIco();
	
	/** 副官ID */
	int adjutantId();
	
	/** 区域 */
	int countryId();
	
	/** 游戏币 */
	int currency();
	
	/** 充值币 */
	int gold();
	
	/** 副本(常驻副本-额外副本-偶发副本) */
	byte[] ectypes();
	
	/** 领地 */
	byte[] manors();
	
	/** 聊天频道ID列表 */
	byte[] chatAxns();
	
}
