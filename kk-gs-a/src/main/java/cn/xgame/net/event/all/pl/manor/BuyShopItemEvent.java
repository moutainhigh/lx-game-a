package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.classes.BStatus;
import cn.xgame.a.player.manor.classes.BType;
import cn.xgame.a.player.manor.info.IBuilding;
import cn.xgame.a.player.manor.info.ShopBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;

/**
 * 购买建筑商店道具
 * @author deng		
 * @date 2015-12-11 下午7:23:48
 */
public class BuyShopItemEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		byte index = data.readByte();
		int pid = data.readInt();
		int count = data.readInt();
		
		ManorControl manors = player.getManors();
		manors.update();
		ErrorCode code = null;
		List<IProp> ret = null;
		try {
			
			IBuilding build = manors.getBuildByIndex( index );
			if( build == null || build.getType() != BType.SHOP )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			if( build.getStatus() != BStatus.INSERVICE  )
				throw new Exception( ErrorCode.MANOR_TIME_ISYET.name() );
			
			ShopBuilding building = (ShopBuilding) build;
			// 检测道具是否足够
			AwardInfo goods = building.getItem( pid );
			if( goods == null || goods.getCount() < count )
				throw new Exception( ErrorCode.PROP_LAZYWEIGHT.name() );
			
			// 执行扣除商店道具 
			goods.addCount( -count );
			if( goods.getCount() == 0 )
				building.removeItem( pid );
			
			// 然后发放道具
			ret = player.getDepots().appendProp(pid, count);
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf(e.getMessage());
		}
		
		ByteBuf buffer = buildEmptyPackage(player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( index );
			buffer.writeInt(pid);
			buffer.writeInt(count);
			buffer.writeByte( ret.size() );
			for( IProp prop : ret ){
				prop.putBaseBuffer(buffer);
				prop.buildTransformStream(buffer);
			}
		}
		sendPackage(player.getCtx(), buffer);
	}

}
