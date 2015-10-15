package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.info.Building;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请产出
 * @author deng		
 * @date 2015-10-15 下午4:47:38
 */
public class ApplyGoodsEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		ManorControl manors = player.getManors();

		// 申请前更新一下
		manors.update();
		
		// 获取能有产出的建筑列表
		List<Building> ls = manors.getProduceBuildings();

		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeByte( ls.size() );
		for( Building o : ls ){
			buffer.writeByte( o.getIndex() );
			o.putProduces( buffer );
		}
		sendPackage( player.getCtx(), buffer );
	}

}
