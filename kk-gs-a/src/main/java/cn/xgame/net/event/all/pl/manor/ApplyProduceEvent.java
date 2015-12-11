package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.classes.Goods;
import cn.xgame.a.player.manor.info.ProduceBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.event.IEvent;

/**
 * 申请产出
 * @author deng		
 * @date 2015-10-15 下午4:47:38
 */
public class ApplyProduceEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		ManorControl manors = player.getManors();

		// 申请前更新一下
		manors.update();
		
		// 获取能有产出的建筑列表
		List<ProduceBuilding> ls = manors.getProduceBuildings();

		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeByte( ls.size() );
		for( ProduceBuilding o : ls ){
			buffer.writeByte( o.getIndex() );
			List<Goods> produces = o.getProduces();
			buffer.writeByte( produces.size() );
			for( Goods g : produces ){
				buffer.writeInt( g.getId() );
				buffer.writeInt( (int) g.getCount() );
			}
		}
		sendPackage( player.getCtx(), buffer );
	}

}
