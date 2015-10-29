package cn.xgame.a.player.fleet.info;

import io.netty.buffer.ByteBuf;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.ITransformStream;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.fleet.classes.IStatus;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.u.Player;

/**
 * 一个舰队信息
 * @author deng		
 * @date 2015-9-10 下午10:06:18
 */
public class FleetInfo implements ITransformStream{
	
	// 舰队编号
	private byte			No ;
	
	// 舰队停靠星球ID
	private int				berthSnid;
	
	// 舰船列表
	private List<ShipInfo> 	ships 			= Lists.newArrayList();
	
	// 舰队状态
	private IStatus 		status ;
	
	// 组队频道ID
	private int 			axnId			= -1;
	
	public FleetInfo( byte No ){
		this.No = No;
	}
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeByte( No );
		buffer.writeInt( berthSnid );
		buffer.writeInt( axnId );
		buffer.writeByte( ships.size() );
		for( ShipInfo ship : ships )
			buffer.writeInt( ship.getuId() );
		status.buildTransformStream(buffer);
	}
	
	public List<ShipInfo> getShips() { return ships; }
	public IStatus getStatus() { return status; }
	public void setStatus( IStatus status ){ this.status = status; }
	public int getAxnId() { return axnId; }
	public void setAxnId(int axnId) { this.axnId = axnId; }
	public byte getNo() { return No; }
	
	public void setBerthSnid( int berthSnid ) { 
		this.berthSnid = berthSnid;
		for( ShipInfo ship : ships )
			ship.setBerthSid(berthSnid);
	}
	public int getBerthSnid() {
		return berthSnid;
	}
	
	public ShipInfo getShip( int suid ){
		for( ShipInfo ship : ships ){
			if( ship.getuId() == suid )
				return ship;
		}
		return null;
	}
	
	/**
	 * 添加一个舰船到这个舰队
	 * @param ship
	 */
	public void addShip( ShipInfo ship ) {
		ships.add( ship );
	}
	
	/**
	 * 从这个舰队删除一个舰船
	 * @param ship
	 */
	public void remove( ShipInfo ship ) {
		Iterator<ShipInfo> iter = ships.iterator();
		while (iter.hasNext()) {
			if( iter.next().getuId() == ship.getuId() ) {
				iter.remove();
				break;
			}
		}
	}
	public void removeAll() {
		ships.clear();
	}
	
	/**
	 * 舰队是否空
	 */
	public boolean isEmpty(){
		return ships.isEmpty();
	}
	
	public boolean isHover() {
		return status.type() == StatusType.HOVER;
	}
	public boolean isSail() {
		return status.type() == StatusType.SAIL;
	}
	public boolean isCombat() {
		return status.type() == StatusType.COMBAT;
	}

	/**
	 * 获取当前耐久和总耐久
	 * @return
	 */
	public int[] getAllDur() {
		int[] ret = new int[2];
		for( ShipInfo ship : ships ){
			ret[0] += ship.getCurrentHp();
			ret[1] += ship.attr().getMaxHp();
		}
		return ret;
	}
	
	/**
	 * 刷新状态结果  - 一般在登录的时候调用
	 * @param player
	 */
	public void updateStatus( Player player ) {
		if( !status.isComplete() ) 
			return;
		status.update( this, player );
	}
	
	/**
	 * 切换状态
	 * @param type
	 * @param objects
	 */
	public IStatus changeStatus( StatusType type, Object ...objects ) {
		status = type.create( );
		status.init( objects );
		return status;
	}
	
	/**
	 * 返回一个战斗者
	 * @return
	 */
	public Fighter fighter(){
		Fighter fighter = new Fighter();
		for( ShipInfo ship : ships ){
			fighter.hp += ship.getCurrentHp();
			ship.wrapAttackattr( fighter );
		}
		return fighter;
	}

}
