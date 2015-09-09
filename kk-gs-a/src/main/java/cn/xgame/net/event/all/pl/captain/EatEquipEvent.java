package cn.xgame.net.event.all.pl.captain;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.captain.o.CaptainInfo;
import cn.xgame.a.player.depot.PlayerDepot;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.cequip.CEquipAttr;
import cn.xgame.net.event.IEvent;

/**
 * 吃芯片
 * @author deng		
 * @date 2015-9-8 下午6:57:15
 */
public class EatEquipEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int cuid = data.readInt();
		int puid = data.readInt();
		
		ErrorCode code = null;
		
		try {
			
			CaptainInfo captain = player.getCaptains().getCaptainOfException(cuid);
			PlayerDepot depots = player.getDepots( captain.getSnid() );
			IProp prop = depots.getPropOfException(puid);
			if( !prop.isCaptainEquip() )
				throw new Exception( ErrorCode.PROP_NOTEXIST.name() );
			
			// 直接设置装备
			captain.getEquips().setEquip( (CEquipAttr) prop );
			
			// 完后在玩家仓库删除道具
			depots.remove(prop);
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buf = buildEmptyPackage( 10 );
		buf.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buf.writeInt( cuid );
			buf.writeInt( puid );
		}
		sendPackage( player.getCtx(), buf );
		
	}

}
