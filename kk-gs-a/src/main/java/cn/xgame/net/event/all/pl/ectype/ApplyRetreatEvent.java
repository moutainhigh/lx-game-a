package cn.xgame.net.event.all.pl.ectype;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.dock.capt.CaptainInfo;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.info.SEquipAttr;
import cn.xgame.net.event.IEvent;

/**
 * 申请撤退
 * @author deng		
 * @date 2015-11-3 下午3:06:01
 */
public class ApplyRetreatEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte fid = data.readByte();// 出击舰队ID

		ErrorCode code 		= null;
		FleetInfo fleet 	= null;
		try {
			fleet = player.getFleets().getFleetInfo(fid);
			if( fleet == null || fleet.isEmpty() || !fleet.isCombat() )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 设置为悬停
			fleet.changeStatus( StatusType.HOVER );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 1024 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( fid );
			fleet.getStatus().buildTransformStream(buffer);
			putWardamaged( player, fleet, buffer );
		}
		sendPackage( player.getCtx(), buffer );
	}

	/**
	 * 塞入战损
	 * @param player
	 * @param fleet
	 * @param buffer
	 */
	private void putWardamaged(Player player, FleetInfo fleet, ByteBuf buffer) {
		List<ShipInfo> ships = fleet.getShips();
		buffer.writeByte( ships.size() );
		for( ShipInfo ship : ships ){
			buffer.writeInt( ship.getuId() );
			buffer.writeInt( ship.getCurrentHp() );
			List<IProp> props = ship.getAllEquip();
			for( IProp prop : props ){
				SEquipAttr weapon = (SEquipAttr) prop;
				buffer.writeInt( weapon.getUid() );
				buffer.writeInt( weapon.getCurrentDur() );
			}
			CaptainInfo capt = player.getDocks().getCaptain( ship.getCaptainUID() );
			buffer.writeInt( capt == null ? 0 : capt.attr().getLoyalty() );
		}
	}
}
