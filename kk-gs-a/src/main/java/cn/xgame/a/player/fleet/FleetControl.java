package cn.xgame.a.player.fleet;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.fleet.o.FleetInfo;
import cn.xgame.a.player.u.Player;

/**
 * 舰队操作中心
 * @author deng		
 * @date 2015-9-10 下午11:47:20
 */
public class FleetControl {
	
	@SuppressWarnings("unused")
	private Player root;
	
	// 舰队列表
	private List<FleetInfo> fleets = Lists.newArrayList();
	
	
	public FleetControl( Player player ) {
		this.root = player;
	}
	
	public List<FleetInfo> getFleet(){ return fleets; }
	
	/**
	 * 根据舰队ID获取舰队信息 - 舰队ID就是舰队列表下标
	 * @param index
	 * @return
	 */
	public FleetInfo getFleetInfo( int index ) {
		if( index < 0 || index >= fleets.size() )
			return null;
		return fleets.get(index);
	}
	
	/**
	 * 根据舰船获取 舰队信息
	 * @param ship
	 * @return
	 */
	public FleetInfo getFleetInfo( ShipInfo ship ) {
		return getFleetInfo( getIndex(ship) );
	}
	
	/**
	 * 根据舰船 获取 舰船所在那个舰队
	 * @param ship
	 * @return
	 */
	public int getIndex( ShipInfo ship ) {
		for( int i = 0; i < fleets.size(); i++ ){
			FleetInfo fleet = fleets.get(i);
			if( fleet.isHave( ship.getuId() ) )
				return i;
		}
		return -1;
	}
	
	/**
	 * 添加一个舰队 
	 */
	public void addFleet(){
		fleets.add( new FleetInfo() );
	}
	
}
