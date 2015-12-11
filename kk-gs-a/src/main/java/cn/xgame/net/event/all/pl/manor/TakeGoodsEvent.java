package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.classes.Goods;
import cn.xgame.a.player.manor.info.ProduceBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;

/**
 * 收取建筑产出
 * @author deng		
 * @date 2015-10-16 上午10:26:02
 */
public class TakeGoodsEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte index = data.readByte();
		List<Goods> goods = Lists.newArrayList();
		byte size = data.readByte();
		for( int i = 0; i < size; i++ ){
			Goods g = new Goods();
			g.setId( data.readInt() );
			g.addCount( data.readInt() );
			goods.add(g);
		}
		
		ManorControl manors = player.getManors();
		ErrorCode code = null;
		List<IProp> ret = Lists.newArrayList();
		try {
			// 获取建筑
			ProduceBuilding building = (ProduceBuilding) manors.getBuildByIndex( index );
			if( building == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 扣除资源
			List<Goods> temp = building.deductGoods( goods );
			
			// 然后将资源放入玩家背包
			StarDepot depots = player.getDepots();
			for( Goods g : temp ){
				ret.addAll( depots.appendProp( g.getId(), (int) g.getCount() ) );
			}
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 50 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( index );
			buffer.writeByte( ret.size() );
			for( IProp prop : ret ){
				prop.putBaseBuffer(buffer);
				prop.buildTransformStream(buffer);
			}
		}
		sendPackage( player.getCtx(), buffer );
	}

}
