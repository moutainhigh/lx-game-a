package cn.xgame.gen.o;

/**
 * 所有舰船 数据
 * @author deng		
 * @date 2015-7-9 下午2:44:12
 */
public interface Ships {
	
	/** 服务器ID */
	short gsid();
	
	/** 玩家唯一ID */
	String uname();
	
	/** 唯一ID */
	int uid();
	
	/** 表格ID */
	int nid();
	
	/** 当前血量 */
	int currentHp();
	
	/** 最大血量 */
	int maxHp();
	
	/** 舰长唯一ID */
	int captainUid();
	
	/** 所属舰队 */
	byte fleet();
	
	/** 组队频道ID */
	int teamAxnid();
	
	/** 状态 */
	byte[] statuss();
	
	/** 副本留存信息 */
	byte[] keepinfos();
	
	/** 货仓 */
	byte[] holds();
	
	/** 装备 */
	byte[] equips();
	
}
