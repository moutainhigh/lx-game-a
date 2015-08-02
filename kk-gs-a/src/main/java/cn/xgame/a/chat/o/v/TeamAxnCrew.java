package cn.xgame.a.chat.o.v;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.chat.o.IAxnCrew;

/**
 * 组队频道 玩家信息
 * @author deng		
 * @date 2015-8-2 下午10:05:32
 */
public class TeamAxnCrew extends IAxnCrew{

	// 舰船UID
	private int shipUid = 0;
	
	// 舰船NID
	private int shipNid = 0;
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		super.buildTransformStream(buffer);
		buffer.writeInt( shipNid );
//		Player player = PlayerManager.o.getPlayerFmOnline(getUid());
//		buffer.writeInt( player == null ? -1 : shipUid );
//		if( player != null ){
//			ShipInfo ship = player.getDocks().getShip(shipUid);
//			buffer.writeInt( ship.getStatus().getCurrentSnid() );
//			ship.getEquips().putBaseBuffer(buffer);
//			CaptainInfo captain = player.getCaptains().getCaptain( ship.getCaptainUID() );
//			captain.buildTransformStream(buffer);
//		}
	}
	
	public int getShipUid() {
		return shipUid;
	}
	public void setShipUid(int shipUid) {
		this.shipUid = shipUid;
	}
	public int getShipNid() {
		return shipNid;
	}
	public void setShipNid(int shipNid) {
		this.shipNid = shipNid;
	}
	
}
