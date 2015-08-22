package cn.xgame.gen.o;

/**
 * 所有舰长数据
 * @author deng		
 * @date 2015-7-9 下午2:46:04
 */
public interface Captains {
	
	/** 服务器ID */
	short gsid();
	
	/** 玩家唯一ID */
	String uname();
	
	/** 唯一ID */
	int uid();
	
	/** 表格ID */
	int nid();
	
	/** 品质 */
	byte quality();
	
	/** 所属舰船UID */
	int shipUid();
	
	/** 装备信息 */
	byte[] equips();
}
