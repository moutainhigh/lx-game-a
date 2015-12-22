package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.classes.BStatus;
import cn.xgame.a.player.manor.info.IBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.BbuildingPo;
import cn.xgame.net.event.IEvent;

/**
 * 升级建筑
 * @author deng		
 * @date 2015-10-15 下午2:35:00
 */
public class UpgradeBuildingEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		byte index = data.readByte();
		
		ErrorCode code = null;
		IBuilding building = null;
		List<IProp> ret = null;
		try {
			ManorControl manors = player.getManors();
			manors.update();
			
			// 获取建筑 并 判断是否可以升级
			building = manors.getBuildByIndex( index );
			if( building == null || building.getNextId() == 0 )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			if( building.getStatus() != BStatus.INSERVICE )
				throw new Exception( ErrorCode.MANOR_TIME_ISYET.name() );
			
			// 这里根据科技判断是否可以升级
			// TODO
			
			// 开始扣除资源
			BbuildingPo templet = CsvGen.getBbuildingPo(building.getNextId());
			if( templet == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			ret = player.deductResource( templet.needres, "升级领地建筑" );
			
			// 开始升级
			building.setStatus(BStatus.UPGRADE);
			building.setEndtime((int)(System.currentTimeMillis()/1000)+templet.needtime);
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( index );
			buffer.writeInt( building.getNid() );
//			System.out.println( "建筑时间=" + building.getEndtime() );
			buffer.writeInt( building.getEndtime() );
			buffer.writeInt( player.getCurrency() );
			buffer.writeByte( ret.size() );
			for( IProp prop : ret ){
				buffer.writeInt( prop.getUid() );
				buffer.writeInt( prop.getCount() );
			}
		}
		if( code == ErrorCode.MANOR_TIME_ISYET ){
			buffer.writeByte( index );
			buffer.writeInt( building.getEndtime() );
		}
		sendPackage( player.getCtx(), buffer );
		
	}
	
}
