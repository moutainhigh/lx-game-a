package cn.xgame.gen.o;

/**
 * 频道信息
 * @author deng		
 * @date 2015-10-23 下午9:46:52
 */
public interface AxnInfo {
	
	/** 频道ID  */
	int id();
	
	/** 频道名字  */
	String name();
	
	/** 频道类型  */
	byte type();
	
	/** 玩家列表  */
	byte[] players();
	
	/** 聊天记录  */
	byte[] chatHistory();
}
