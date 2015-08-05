package cn.xgame.a.chat.o;

import io.netty.buffer.ByteBuf;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.ITransformStream;
import cn.xgame.a.chat.o.v.TeamAxnCrew;
import cn.xgame.a.chat.o.v.TempAxnCrew;
import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.ship.o.ShipInfo;
import cn.xgame.a.player.u.Player;

/**
 * 一个频道数据
 * @author deng		
 * @date 2015-8-2 下午12:53:08
 */
public class AxnInfo implements ITransformStream{

	// 频道唯一ID
	private int  		axnId;
	
	// 频道类型
	private ChatType 	type;
	
	// 频道对应玩家列表
	private List<IAxnCrew> axnCrews = Lists.newArrayList();

	public AxnInfo(ChatType type, int axnId) {
		this.type 	= type;
		this.axnId 	= axnId;
	}
	

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( axnId );
		buffer.writeByte( axnCrews.size() );
		for( IAxnCrew crew : axnCrews ){
			crew.buildTransformStream(buffer);
		}
	}
	
	public int getAxnId() { return axnId; }
	public ChatType getType() { return type; }
	public List<IAxnCrew> getAxnCrews() { return axnCrews; }

	/**
	 * 人数是否已经满了
	 * @return
	 */
	public boolean isMaxmember() {
		return axnCrews.size() >= type.maxmember();
	}
	
	/**
	 * 玩家是否有权限
	 * @param player
	 * @return
	 */
	public boolean isHavePrivilege( Player player ) {
		return getAxnCrew( player.getUID() ) != null;
	}
	
	/**
	 * 根据玩家UID 获取玩家信息
	 * @param uid
	 * @return
	 */
	public IAxnCrew getAxnCrew( String uid ){
		for( IAxnCrew crew : axnCrews ){
			if( crew.getUid().equals(uid) )
				return crew;
		}
		return null;
	}
	
	/**
	 * 添加临时频道 组员
	 * @param player
	 */
	public void appendTempCrew( Player player ) {
		if( getAxnCrew( player.getUID() ) != null )
			return ;
		TempAxnCrew crew = new TempAxnCrew();
		crew.setUid( player.getUID() );
		crew.setName( player.getNickname() );
		crew.setHeadIco( player.getHeadIco() );
		crew.setSocket( player.getCtx() );
		axnCrews.add(crew);
	}

	/**
	 * 添加组队频道 组员
	 * @param player
	 * @param mship
	 */
	public void appendTeamCrew(Player player, ShipInfo ship) {
		if( getAxnCrew( player.getUID() ) != null )
			return ;
		TeamAxnCrew crew = new TeamAxnCrew();
		crew.setUid( player.getUID() );
		crew.setName( player.getNickname() );
		crew.setHeadIco( player.getHeadIco() );
		crew.setSocket( player.getCtx() );
		crew.setShipUid( ship.getuId() );
		crew.setShipNid( ship.getnId() );
		axnCrews.add(crew);
	}

	/**
	 * 删除一个组员
	 * @param uid
	 */
	private void removeAxnCrew( String uid ) {
		Iterator<IAxnCrew> iter = axnCrews.iterator();
		while( iter.hasNext() ){
			if( iter.next().getUid().equals( uid ) ){
				iter.remove();
				return ;
			}
		}
	}
	
	/**
	 * 玩家退出
	 * @param player
	 * @return
	 */
	public boolean exit( Player player ) {
		player.getChatAxns().removeAxn( axnId );
		removeAxnCrew( player.getUID() );
		// 这里还剩最后一个的时候 这个频道就不存在了
		if( axnCrews.size() == 1 ){
			Player o = PlayerManager.o.getPlayerFmOnline( axnCrews.get(0).getUid() );
			if( o != null ) o.getChatAxns().removeAxn(axnId);
		}
		return axnCrews.size() <= 1;
	}

	/**
	 * 玩家离线
	 * @param player
	 * @return
	 */
	public void offline( Player player ) {
		exit( player );
	}
	
	
}
