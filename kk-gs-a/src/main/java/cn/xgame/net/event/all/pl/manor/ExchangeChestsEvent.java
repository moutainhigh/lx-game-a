package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.Util.Random;

import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.classes.BStatus;
import cn.xgame.a.player.manor.classes.BType;
import cn.xgame.a.player.manor.info.IBuilding;
import cn.xgame.a.player.manor.info.PedleryBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.TreasurePo;
import cn.xgame.net.event.IEvent;

/**
 * 兑换宝箱
 * @author deng		
 * @date 2015-12-11 下午7:32:18
 */
public class ExchangeChestsEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte index = data.readByte();
		int tid = data.readInt();
		
		ManorControl manors = player.getManors();
		manors.update();
		ErrorCode code = null;
		List<IProp> ret = Lists.newArrayList();
		int propid = 0;
		int propcount = 0;
		try {
			IBuilding build = manors.getBuildByIndex( index );
			if( build == null || build.getType() != BType.PEDIERY )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			if( build.getStatus() != BStatus.INSERVICE  )
				throw new Exception( ErrorCode.MANOR_TIME_ISYET.name() );
			
			PedleryBuilding building = (PedleryBuilding) build;
			// 检测是否有该宝箱
			if( building.getTreasures().indexOf(tid) == -1 )
				throw new Exception( ErrorCode.PROP_NOTEXIST.name() );
			TreasurePo templet = CsvGen.getTreasurePo(tid);
			if( templet == null )
				throw new Exception( ErrorCode.PROP_NOTEXIST.name() );
			
			// 开始扣除道具
			ret = player.deductResource( templet.needres, "兑换宝箱" );
			
			// 开始随机道具
			String[] array = templet.content.split("\\|");
			int idx = Random.get( 0, array.length-1 );
			String[] str = array[idx].split(";");
			propid 		= Integer.parseInt( str[0] );
			propcount 	= Integer.parseInt( str[1] );
			
			// 开始发放道具
			ret.addAll( player.getDepots().appendProp(propid, propcount) );
			
			// 最后在行商列表删除该宝箱
			building.removeChests( tid );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage(player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( index );
			buffer.writeInt( tid );
			buffer.writeInt( propid );
			buffer.writeInt( propcount );
			buffer.writeByte( ret.size() );
			for( IProp prop : ret ){
				prop.putBaseBuffer(buffer);
				prop.buildTransformStream(buffer);
			}
		}
		sendPackage(player.getCtx(), buffer);
		
	}
	
}
