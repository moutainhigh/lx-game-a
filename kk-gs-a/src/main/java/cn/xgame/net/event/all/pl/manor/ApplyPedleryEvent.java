package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.classes.BStatus;
import cn.xgame.a.player.manor.classes.BType;
import cn.xgame.a.player.manor.info.IBuilding;
import cn.xgame.a.player.manor.info.PedleryBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请行商
 * @author deng		
 * @date 2015-12-11 下午6:58:01
 */
public class ApplyPedleryEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte index = data.readByte();
		
		ManorControl manors = player.getManors();
		manors.update();
		ErrorCode code = null;
		PedleryBuilding building = null;
		try {
			IBuilding build = manors.getBuildByIndex( index );
			if( build == null || build.getType() != BType.PEDIERY )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			if( build.getStatus() != BStatus.INSERVICE  )
				throw new Exception( ErrorCode.MANOR_TIME_ISYET.name() );
			
			building = (PedleryBuilding) build;
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( building.getIndex() );
			buffer.writeByte( building.getUpdatelevel() );
			buffer.writeByte( building.getGoodclevel() );
			buffer.writeByte( building.getGoodqlevel() );
			buffer.writeByte( building.getTreasures().size() );
			for( int id : building.getTreasures() ){
				buffer.writeInt(id);
			}
			buffer.writeInt( building.getNextUpdateTime() );
		}
		sendPackage( player.getCtx(), buffer );
		
	}

}
