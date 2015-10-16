package cn.xgame.net.event.all.pl.manor;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.info.UnBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;
import cn.xgame.system.LXConstants;
import cn.xgame.utils.Logs;

/**
 * 建造 建筑
 * @author deng		
 * @date 2015-10-15 下午12:22:32
 */
public class BuildBuildingEvent extends IEvent {

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int bnid = data.readInt();
		byte index = data.readByte();
		
		ErrorCode code = null;
		UnBuilding building = null;
		List<IProp> ret = null;
		try {
			// 生成建筑
			building = new UnBuilding( bnid );
			if( building.templet() == null )
				throw new Exception( ErrorCode.OTHER_ERROR.name() );
			building.setIndex(index);
			
			ManorControl manors = player.getManors();
			// 检测该位置是否可以建造
			manors.isCanBuild( building );
			// 检测科技等级是否满足
			// TODO
			
			// 这里开始扣除资源
			ret = deductResource( player, building.templet().needres );
			
			// 开始建造
			building.inBuild();
			
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
	 * 扣除资源
	 * @param player
	 * @param needres
	 * @throws Exception 
	 */
	private List<IProp> deductResource( Player player, String needres ) throws Exception {
		int needMoney 			= 0;
		List<IProp> materials 	= Lists.newArrayList();
		StarDepot depots 		= player.getDepots();
		// 先判断是够足够
		String[] array 		= needres.split( "\\|" );
		for( String str : array ){
			String[] res 	= str.split(";");
			int id 			= Integer.parseInt( res[0] );
			int count 		= Integer.parseInt( res[1] );
			if( id == LXConstants.CURRENCY_NID ){
				if( count > player.getCurrency() )
					throw new Exception( ErrorCode.CURRENCY_LAZYWEIGHT.name() );
				needMoney 	+= count;
			}else{
				// 拷贝一份当前资源道具列表 然后虚拟扣除
				List<IProp> temp = depots.getPropsByNidClone( id );
				for( IProp prop : temp ){
					count	= prop.deductCount( count );
					materials.add( prop );
					if( count == 0 ) break;
				}
				// 最后如果资源没扣完 那么说明数量不足 直接返回
				if( count > 0 ) 
					throw new Exception( ErrorCode.STUFF_LAZYWEIGHT.name() );
			}
		}
		// 这里执行扣除
		player.changeCurrency( -needMoney );
		for( IProp prop : materials ){
			if( prop.isEmpty() )
				depots.remove( prop );
			else
				depots.getProp( prop ).setCount( prop.getCount() );
		}
		Logs.debug( player, "领地建筑 扣除资源 " + materials );
		return materials;
	}

}
