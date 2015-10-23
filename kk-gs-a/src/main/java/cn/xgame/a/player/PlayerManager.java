package cn.xgame.a.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import x.javaplus.collections.Lists;
import x.javaplus.mysql.db.Condition;
import x.javaplus.util.ErrorCode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.player.u.classes.Init;
import cn.xgame.gen.dto.MysqlGen.PlayerDataDao;
import cn.xgame.gen.dto.MysqlGen.PlayerDataDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.net.event.Events;
import cn.xgame.system.SystemCfg;
import cn.xgame.utils.Illegality;

public class PlayerManager {
	
	private PlayerManager(){}
	public static PlayerManager o = new PlayerManager();
	
	// 在线玩家
	private Map<String, Player> players = new HashMap<String, Player>();
	
	// 离线玩家 - 这里暂时为组队
	private List<Player> offline = Lists.newArrayList();
	
	
	/**
	 * 针对玩家 的 消息分发
	 * @param uID
	 * @param event
	 * @param bufferData
	 * @throws Exception 
	 */
	public void eventRun( String uID, Events event, ByteBuf data ) throws Exception {
		
		Player player = getPlayerFmOnline( uID );
		if( player == null )
			throw new Exception( "UID无效,UID="+uID );
		
		if( player.safeCheck( event ) )
			throw new Exception( "出现恶意刷包,UID="+uID );
		
		// 分发给对应 包处理
		event.run( player, data );
	}
	
	public Map<String, Player> getOnlinePlayer(){
		return players;
	}
	
	/**
	 * 获取在线人数
	 * @return
	 */
	public int onlinePeople() {
		return players.size();
	}
	
	/**
	 * 从在线列表 获取 玩家 
	 * @param uID
	 * @return
	 */
	public Player getPlayerFmOnline( String uID ){
		return players.get(uID);
	}
	
	/**
	 * 获取 玩家  没有会在数据库获取
	 * @param uID
	 * @param gsid 
	 * @return
	 */
	public Player getPlayer( String uID, short gsid ) {
		Player ret = getPlayerByTeam( uID );
		if( ret == null )
			ret = getPlayerFmDB( uID, gsid );
		return ret;
	}
	
	/**
	 * 专门为组队开发一个获取接口
	 * @param uid
	 * @return
	 */
	public Player getPlayerByTeam( String uid ) {
		Player ret = getPlayerFmOnline( uid );
		if( ret == null ){
			for( Player o : offline ){
				if( o.getUID().equals( uid ) )
					return o;
			}
		}
		return null;
	}
	
	//===============================================================
	//TODO=========================其他函数==========================
	//===============================================================
	
	
	public void allDailyHandle(){
		for( Player player : players.values() ){
			dailyHandle( player, 0 );
			PlayerManager.o.update(player);
		}
	}
	
	/**
	 * 处理每日
	 * @param ret
	 * @param day (跨的天数)
	 */
	private void dailyHandle( Player player, int day ) {
		
		// 初始所有副本次数
		player.getEctypes().initAllTimes();
		
		// 结算舰长周薪
		player.getDocks().balanceWeekly();
		
	}

	//===============================================================
	//TODO=========================通信相关==========================
	//===============================================================
	
	/**
	 * 玩家 登录
	 * @param ctx 
	 * @param uID
	 * @return
	 */
	public Player login( ChannelHandlerContext ctx, String uID ) throws Exception{
		
		Player ret = getPlayer( uID, SystemCfg.ID );
		if( ret == null )
			throw new Exception( ErrorCode.PLAYER_NOTFOUND.name() );
		
		// 设置socket
		ret.setCtx( ctx );
		// 放入内存
		players.put( ret.getUID(), ret );
		
		// 判断是否过天
		int day = ret.strideDay();
		if( day >= 0 ) {
			dailyHandle( ret, day );
			// 刷新一下 普通限时副本 根据玩家登录限时
			ret.getEctypes().updateNormalEctype();
		}else{
			// 这里是为了方便测试 让每次登录都 刷新副本
			ret.getEctypes().updateNormalEctype();
		}
		
		return ret;
	}
	

	/**
	 * 玩家 创建
	 * @param ctx 
	 * @param uID
	 * @param name 
	 * @param headIco 
	 * @param adjutantId 
	 * @param countryId 
	 * @return
	 */
	public Player create( ChannelHandlerContext ctx, String uID, int headIco, int adjutantId, String name, int countryId ) throws Exception{
		
		// 过滤非法字
		if( Illegality.own( name ) )
			throw new Exception( ErrorCode.HAVE_ILLEGALITY.name() );
		
		// 判断是否重复
		if( isRepeatName( name ) )
			throw new Exception( ErrorCode.NAME_REPEAT.name() );
		
		Player ret = null;
		try {
			// 初始化数据
			ret = Init.run( uID, headIco, adjutantId, name, countryId );
			// 设置socket
			ret.setCtx( ctx );
			// 放入内存
			players.put( ret.getUID(), ret );
			
			// 最后在数据库创建
			create( ret );
		} catch (Exception e) {
			throw new Exception( ErrorCode.OTHER_ERROR.name() );
		}
		
		return ret;
	}

	/**
	 * 玩家 退出
	 * @param uID
	 */
	public void exit( String uID ) {
		// 先从内存中直接删除
		Player player = players.remove(uID);
		if( player == null )
			return;
		player.setLastLogoutTime( System.currentTimeMillis() );
		
		// 这里看他是不是有组队  有就伪离线
		if( player.getFleets().isHaveTeam() ){
			
			offline.add( player );
		}else{
			
			// 然后处理退出
			player.exit();
			
			// 最后保存数据库
			update( player );
		}
	}

	/**
	 * 退出那些离线的
	 * @param temp
	 */
	public void exitByOffline( Player player ) {
		// 然后处理退出
		player.exit();
		
		// 最后保存数据库
		update( player );
		
		offline.remove(player);
	}

	
	//===============================================================
	//TODO========================数据库相关==========================
	//===============================================================
	
	/**
	 * 创建
	 * @param player
	 */
	public void create( Player player ) {
		PlayerDataDao dao = SqlUtil.getPlayerDataDao();
		PlayerDataDto dto = dao.create();
		player.update( dto );
		dao.commit(dto);
	}
	
	/**
	 * 保存到数据库
	 * @param player
	 */
	public void update( Player player ) {
		PlayerDataDao dao = SqlUtil.getPlayerDataDao();
		String sql = new Condition( PlayerDataDto.uidChangeSql(player.getUID()) ).AND( PlayerDataDto.gsidChangeSql(player.getGsid()) ).toString();
		PlayerDataDto dto = dao.updateByExact( sql );
		player.update( dto );
		dao.commit(dto);
	}
	
	/**
	 * 从数据库 获取玩家
	 * @param uID
	 * @return
	 */
	public Player getPlayerFmDB( String uID, short gsid ) {
		PlayerDataDao dao = SqlUtil.getPlayerDataDao();
		String sql = new Condition( PlayerDataDto.uidChangeSql(uID) ).AND( PlayerDataDto.gsidChangeSql(gsid) ).toString();
		List<PlayerDataDto> dtos = dao.getByExact( sql );
		Player ret = dtos.isEmpty() ? null : new Player( dtos.get(0) );
		dao.commit();
		return ret;
	}

	/**
	 * 在数据库  查找 是否有该名字
	 * @param name
	 * @return
	 */
	public boolean isRepeatName( String name ) {
		PlayerDataDao dao = SqlUtil.getPlayerDataDao();
		List<PlayerDataDto> dto = dao.getByExact( PlayerDataDto.nicknameChangeSql(name) );
		dao.commit();
		return !dto.isEmpty();
	}
	
	/**
	 * 获取服务器总人数
	 * @return
	 */
	public int peopleNumber() {
		return (int) SqlUtil.getCount( SqlUtil.getClassName( PlayerDataDto.class ) );
	}
	
	public static void main(String[] args) throws Exception {
		
		try {
			
//			o.create( null, "102", 1, "大峰哥3" );
			
			System.out.println( o.peopleNumber() );
			
		} catch (Exception e) {
		}
		
		
	}


}
