package cn.xgame.a.chat.axn.classes;

import java.util.HashMap;
import java.util.Map;

import cn.xgame.system.LXConstants;


/**
 * 聊天频道
 * @author deng		
 * @date 2015-7-25 上午10:06:36
 */
public enum ChatType {

	/** 世界频道 */
	WORLD(1) {
		@Override
		public int max() { return 1; }

		@Override
		public int maxmember() { return Integer.MAX_VALUE; }

		@Override
		public int generateUID() { return 1*LXConstants.CHAT_UID + 1; }

		@Override
		public void appendUID(int id) { }
	},
	
	/** 母星频道 */
	PLANET(2) {
		@Override
		public int max() { return 1; }

		@Override
		public int maxmember() { return Integer.MAX_VALUE; }
		
		@Override
		public int generateUID() { return 2*LXConstants.CHAT_UID + 1; }

		@Override
		public void appendUID(int id) { }
	},
	
	/** 队伍频道 */
	TEAM(3) {
		@Override
		public int max() { return LXConstants.TEAMAXN_MAX; }

		@Override
		public int maxmember() { return LXConstants.TEAMAXN_MAXMEMBER; }
		
		@Override
		public int generateUID() throws Exception { return ChatAxnUID.getTeamaxnUid(); }

		@Override
		public void appendUID(int id) { ChatAxnUID.appendTeamUid(id); }
	},
	
	/** 群聊频道 */
	GROUP(4) {
		@Override
		public int max() { return LXConstants.TEMPAXN_MAX; }
		
		@Override
		public int maxmember() { return LXConstants.TEMPAXN_MAXMEMBER; }

		@Override
		public int generateUID() throws Exception { return ChatAxnUID.getTempaxnUid(); }

		@Override
		public void appendUID(int id) { ChatAxnUID.appendTempUid(id); }
	},
	
	/** 私聊频道 */
	PRIVATE(5) {
		@Override
		public int max() { return LXConstants.PRIVATEAXN_MAX; }
		
		@Override
		public int maxmember() { return 2; }
		
		@Override
		public int generateUID() throws Exception { return ChatAxnUID.getTempaxnUid(); }
		
		@Override
		public void appendUID(int id) { ChatAxnUID.appendTempUid(id); }
	};
	
	
	private final byte	number;
	private static final Map<Byte, ChatType> numToEnum = new HashMap<Byte, ChatType>();
	
	ChatType( int n ){
		number = (byte) n;
	}

	static{
		for( ChatType a : values() ){
			ChatType p = numToEnum.put( a.number, a );
			if( p != null ){
				throw new RuntimeException( a.number + "重复了" );
			}
		}
	}
	
	public byte toNumber(){ return number; }
	public static ChatType fromNumber( int n ){
		return numToEnum.get( (byte)n );
	}
	
	/**
	 * 获取频道最大拥有个数
	 * @return
	 */
	public abstract int max();
	
	/**
	 * 获取频道人数上限
	 * @return
	 */
	public abstract int maxmember();
	
	/**
	 * 生成一个唯一ID
	 * @return
	 * @throws Exception 
	 */
	public abstract int generateUID() throws Exception;
	
	/**
	 * 添加一个UID到临时存放中
	 * @param id
	 */
	public abstract void appendUID( int id );


}
