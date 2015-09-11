package cn.xgame.a.player.fleet.o;

import io.netty.buffer.ByteBuf;

import java.util.List;

import cn.xgame.a.ITransformStream;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.fleet.other.IStatus;
import cn.xgame.a.player.fleet.other.StatusType;

import x.javaplus.collections.Lists;

/**
 * 一个舰队信息
 * @author deng		
 * @date 2015-9-10 下午10:06:18
 */
public class FleetInfo implements ITransformStream{
	
	// 舰船列表
	private List<ShipInfo> ships = Lists.newArrayList();
	
	// 舰队状态
	private IStatus status 		= StatusType.LEISURE.create();
	
	
	// 当前停靠星球ID
	private int berthSnid;
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeByte( ships.size() );
		for( ShipInfo ship : ships )
			buffer.writeInt( ship.getuId() );
		status.buildTransformStream( buffer );
	}
	
	public List<ShipInfo> getShips() {
		return ships;
	}
	public IStatus getStatus() {
		return status;
	}
	public void setStatus( IStatus status ){
		this.status = status;
	}
	public int getBerthSnid() {
		return berthSnid;
	}
	public void setBerthSnid(int berthSnid) {
		this.berthSnid = berthSnid;
	}
	
	public ShipInfo getShip( int suid ){
		for( ShipInfo ship : ships ){
			if( ship.getuId() == suid )
				return ship;
		}
		return null;
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
