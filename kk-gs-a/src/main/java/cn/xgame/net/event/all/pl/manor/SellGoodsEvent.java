package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.classes.Goods;
import cn.xgame.a.player.manor.info.ProduceBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ItemPo;
import cn.xgame.net.event.IEvent;

/**
 * 卖出产出
 * @author deng		
 * @date 2015-11-3 下午2:30:45
 */
public class SellGoodsEvent extends IEvent{

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
		
		ErrorCode code = null;
		try {
			ManorControl manors = player.getManors();

			// 获取建筑
			ProduceBuilding building = (ProduceBuilding) manors.getBuildByIndex( index );
			if( building == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			
			// 扣除资源
			List<Goods> temp = building.deductGoods( goods );
			
			// 算出产出一共出售的钱
			int money = 0;
			for( Goods g : temp ){
				ItemPo item = CsvGen.getItemPo(g.getId());
				money += (item.sellgold * g.getCount());
			}
			
			// 加入到玩家身上
			player.changeCurrency(money, "出售产出添加");
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 50 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( index );
			buffer.writeInt( player.getCurrency() );
		}
		sendPackage( player.getCtx(), buffer );
		
	}

}
