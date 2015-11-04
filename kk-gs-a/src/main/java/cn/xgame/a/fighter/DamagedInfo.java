package cn.xgame.a.fighter;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.dock.capt.CaptainInfo;
import cn.xgame.a.player.dock.ship.ShipInfo;

/**
 * 战损信息  主要用于发送前端数据
 * @author deng		
 * @date 2015-11-5 上午5:38:09
 */
public class DamagedInfo implements ITransformStream{

	private List<LossShip> LossShips = Lists.newArrayList();
	
	private List<LossEquip> LossEquips = Lists.newArrayList();
	
	private List<LossCapt> LossCapts = Lists.newArrayList();
	
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( LossShips.size() );
		for( LossShip x : LossShips ){
			buffer.writeInt( x.uid );
			buffer.writeInt( x.curhp );
		}
		buffer.writeByte( LossEquips.size() );
		for( LossEquip x : LossEquips ){
			buffer.writeInt( x.suid );
			buffer.writeInt( x.uid );
			buffer.writeInt( x.curdur );
		}
		buffer.writeByte( LossCapts.size() );
		for( LossCapt x : LossCapts ){
			buffer.writeInt( x.suid );
			buffer.writeInt( x.loyalty );
		}
	}

	/**
	 * 添加损耗的船
	 * @param ship
	 */
	public void addLossShip( ShipInfo ship ) {
		LossShip x = new LossShip();
		x.uid = ship.getuId();
		x.curhp = ship.getCurrentHp();
		LossShips.add(x);
	}

	/**
	 * 添加损耗的舰船装备
	 * @param ship
	 * @param equip
	 */
	public void addLossEquip( ShipInfo ship, int uid, int curdur ) {
		LossEquip x = new LossEquip();
		x.suid = ship.getuId();
		x.uid = uid;
		x.curdur = curdur;
		LossEquips.add(x);
	}

	/**
	 * 添加损耗的舰长
	 * @param ship
	 * @param capt
	 */
	public void addLossCapt( ShipInfo ship, CaptainInfo capt ) {
		LossCapt x = new LossCapt();
		x.suid = ship.getuId();
		x.loyalty = capt.attr().getLoyalty();
		LossCapts.add(x);
	}

}

class LossShip{
	int uid;
	int curhp;
}

class LossEquip{
	int suid;
	int uid;
	int curdur;
}

class LossCapt{
	int suid;
	int loyalty;
}
