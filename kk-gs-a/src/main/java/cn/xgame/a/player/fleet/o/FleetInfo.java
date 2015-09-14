package cn.xgame.a.player.fleet.o;

import io.netty.buffer.ByteBuf;

import java.util.Iterator;
import java.util.List;

import cn.xgame.a.ITransformStream;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.fleet.other.IStatus;
import cn.xgame.a.player.fleet.other.StatusType;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;

/**
 * 一个舰队信息
 * @author deng		
 * @date 2015-9-10 下午10:06:18
 */
public class FleetInfo implements ITransformStream{
	
	// 舰船列表
	private List<ShipInfo> 	ships 			= Lists.newArrayList();
	
	// 舰队状态
	private IStatus 		status 			= StatusType.LEISURE.create();
	
	// 当前停靠星球ID
	private int 			berthSnid		= -1;
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeInt( berthSnid );
		buffer.writeByte( ships.size() );
		for( ShipInfo ship : ships )
			buffer.writeInt( ship.getuId() );
		status.buildTransformStream(buffer);
	}
	
	public List<ShipInfo> getShips() { return ships; }
	public IStatus getStatus() { return status; }
	public void setStatus( IStatus status ){ this.status = status; }
	public int getBerthSnid() { return berthSnid; }
	public void setBerthSnid(int berthSnid) { this.berthSnid = berthSnid; }
	
	public ShipInfo getShip( int suid ){
		for( ShipInfo ship : ships ){
			if( ship.getuId() == suid )
				return ship;
		}
		return null;
	}
	
	/**
	 * 是否空闲状态
	 * @return
	 * @throws Exception
	 */
	public boolean isLeisure() throws Exception {
		if( status.type() == StatusType.SAIL || status.type() == StatusType.COMBAT )
			throw new Exception( ErrorCode.SHIP_NOTLEISURE.name() );
		return true;
	}
	
	/**
	 * 添加一个舰船到这个舰队
	 * @param ship
	 */
	public void add( ShipInfo ship ) {
		if( getShip(ship.getuId()) != null )
			return;
		ships.add(ship);
		// 如果还是空闲状态 那么设置为悬停
		if( status.type() == StatusType.LEISURE ){
			status 		= StatusType.HOVER.create();
			berthSnid 	= ship.getBerthSid();
		}
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
		// 如果没有舰船在这个舰队了上了 那么就要设置为空闲状态
		if( ships.isEmpty() ){
			status 		= StatusType.LEISURE.create();
			berthSnid 	= -1;
		}
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
