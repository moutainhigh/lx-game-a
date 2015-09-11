package cn.xgame.a.player.fleet.o;

import io.netty.buffer.ByteBuf;

import java.util.List;

import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.fleet.other.IStatus;
import cn.xgame.a.player.fleet.status.LeisureStatus;

import x.javaplus.collections.Lists;

/**
 * 一个舰队信息
 * @author deng		
 * @date 2015-9-10 下午10:06:18
 */
public class FleetInfo implements ITransformStream{
	
	// 舰船列表
	private List<Integer> ships = Lists.newArrayList();

	// 舰队状态
	private IStatus status 		= new LeisureStatus();
	
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeByte( ships.size() );
		for( int id : ships )
			buffer.writeInt( id );
		buffer.writeByte( status.type().toNumber() );
		status.buildTransformStream( buffer );
	}
	
	public List<Integer> getShips() {
		return ships;
	}
	public IStatus getStatus() {
		return status;
	}
	
	/**
	 * 是否有这艘舰船
	 * @param suid
	 * @return
	 */
	public boolean isHave( int suid ) {
		return ships.indexOf(suid) != -1;
	}

	
	
	
}
