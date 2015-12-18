package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.string.StringUtil;
import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.classes.BStatus;
import cn.xgame.a.player.manor.classes.BType;
import cn.xgame.a.player.manor.info.IBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.BbuildingPo;
import cn.xgame.config.o.ReclaimPo;
import cn.xgame.net.event.IEvent;

/**
 * 建造 建筑
 * @author deng		
 * @date 2015-10-15 下午12:22:32
 */
public class BuildBuildingEvent extends IEvent {
	
	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int bnid 	= data.readInt();
		byte index 	= data.readByte();
		
		ManorControl manors = player.getManors();
		ErrorCode code = null;
		IBuilding building = null;
		List<IProp> ret = null;
		try {
			BbuildingPo templet = CsvGen.getBbuildingPo(bnid);
			if( templet == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			// 先刷新一下 因为可能有的建筑已经建筑好了
			manors.update();

			// 检测是否可以建造 位置-重复-总空间
			isCanBuild( templet, index, manors.getBuilds(), manors.getTerritory() );
			// 检测前置
			isFront(templet, manors.getBuilds());
			// 检测科技等级是否满足
			// TODO
			
			// 这里开始扣除资源
			ret = player.deductResource( templet.needres, "建造领地建筑" );
			
			// 创建建筑
			BType bType = BType.values()[templet.buildtype-1];
			building = bType.create(templet);
			building.setIndex(index);
			building.setStatus(BStatus.IMBAU);
			building.setEndtime((int)(System.currentTimeMillis()/1000)+templet.needtime);
			
			// 加入建筑列表
			manors.addBuilding( building );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 125 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeInt( bnid );
			buffer.writeByte( index );
			buffer.writeInt( building.getEndtime() );
			buffer.writeInt( player.getCurrency() );
			buffer.writeByte( ret.size() );
			for( IProp prop : ret ){
				buffer.writeInt( prop.getUid() );
				buffer.writeInt( prop.getCount() );
			}
		}
		sendPackage( player.getCtx(), buffer );
	}



	/**
	 * 检测这个建筑是否可以建造   位置-重复-总空间
	 * @param building
	 * @return
	 * @throws Exception 
	 */
	private boolean isCanBuild( BbuildingPo templet, byte index, List<IBuilding> builds, ReclaimPo territory ) throws Exception {
		int sumGrid = templet.usegrid;
		for( IBuilding o : builds ){
			// 看是否允许唯一
//			if( templet.id == o.getNid() )
//				throw new Exception( ErrorCode.HAVE_EQUAL.name() );
			// 看位置是否允许建筑
			if( o.indexIsOverlap( index, templet.usegrid ) )
				throw new Exception( ErrorCode.INDEX_OCCUPY.name() );
			sumGrid += o.getUsegrid();
		}
		// 判断该领地的格子数还够不够
		if( sumGrid > territory.room )
			throw new Exception( ErrorCode.ROOM_LAZYWEIGHT.name() );
		return true;
	}

	/**
	 * 检查建筑前置条件
	 * @param templet
	 * @param builds
	 * @throws Exception 
	 */
	private boolean isFront(BbuildingPo templet, List<IBuilding> builds) throws Exception {
		if( templet.front.isEmpty() )
			return true;
		List<Integer> array = StringUtil.arrayToInteger(templet.front, ";");
		for( IBuilding o : builds ){
			if( array.indexOf( o.getNid() ) != -1 )
				return true;
		}
		throw new Exception( ErrorCode.NEED_FRONT_CONDITION.name() );
	}
	
}
