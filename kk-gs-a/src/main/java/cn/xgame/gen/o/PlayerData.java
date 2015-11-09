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
	
	/** 玩家等级 */
	short level();
	
	/** 玩家经验 */
	int exp();
	
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
	
	/** 酒馆数据 */
	byte[] taverns();
	
	/** 舰队数据 */
	byte[] fleets();
	
	/** 任务数据 */
	byte[] tasks();
	
	/** 兑换数据 */
	byte[] swops();
}
