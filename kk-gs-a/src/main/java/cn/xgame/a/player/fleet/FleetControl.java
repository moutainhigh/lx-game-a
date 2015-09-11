package cn.xgame.a.player.fleet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.fleet.o.FleetInfo;
import cn.xgame.a.player.fleet.other.IStatus;
import cn.xgame.a.player.u.Player;

/**
 * 舰队操作中心
 * @author deng		
 * @date 2015-9-10 下午11:47:20
 */
public class FleetControl implements IArrayStream{
	
	private Player root;
	
	// 舰队列表
	private List<FleetInfo> fleets = Lists.newArrayList();
	
	
	public FleetControl( Player player ) {
		this.root = player;
	}
	
	@Override
	public void fromBytes( byte[] data ) {
		if( data == null ) return ;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			FleetInfo fleet = new FleetInfo();
			List<ShipInfo> ships = fleet.getShips();
			fleet.setBerthSnid( buf.readInt() );
			byte count = buf.readByte();
			for( int j = 0; j < count; j++ ){
				ShipInfo ship = root.getDocks().getShip( buf.readInt() );
				if( ship == null ) continue;
				ships.add( ship );
			}
			fleet.setStatus( IStatus.create( buf ) );
		}
	}
	
	@Override
	public byte[] toBytes() {
		if( fleets.isEmpty() ) return null;
		ByteBuf buf = Unpooled.buffer();
		buf.writeByte( fleets.size() );
		for( FleetInfo fleet : fleets ){
			buf.writeInt( fleet.getBerthSnid() );
			fleet.buildTransformStream(buf);
		}
		return buf.array();
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
			if( fleet.getShip( ship.getuId() ) != null )
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
