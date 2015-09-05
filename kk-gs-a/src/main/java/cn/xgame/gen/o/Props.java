package cn.xgame.gen.o;

/**
 * 所有道具
 * @author deng		
 * @date 2015-7-9 下午1:26:04
 */
public interface Props {

	/** 服务器ID */
	short gsid();
	
	/** 玩家唯一ID */
	String uname();
	
	/** 唯一ID */
	int uid();
	
	/** 表格ID */
	int nid();
	
	/** 数量 */
	int count();
	
	/** 品质 */
	byte quality();
	
	/** 附加数据 */
	byte[] attach();
	
}
