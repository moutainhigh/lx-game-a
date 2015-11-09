package cn.xgame.net.event.all.pl.transaction;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.lua.LuaValue;

import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.swop.SwopManager;
import cn.xgame.a.player.swop.info.SpecialSwop;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.net.event.IEvent;
import cn.xgame.system.LXConstants;
import cn.xgame.utils.LuaUtil;

/**
 * 特殊兑换
 * @author deng		
 * @date 2015-11-6 下午6:16:40
 */
public class SpecialSwopEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		int sid = data.readInt();
		List<SwopReslut> resluts = Lists.newArrayList();
		byte size = data.readByte();
		for( int i = 0; i < size; i++ ){
			SwopReslut swop = new SwopReslut();
			swop.id = data.readInt();
			swop.count = data.readInt();
			resluts.add(swop);
		}
		
		ErrorCode code = null;
		List<IProp> ret = Lists.newArrayList();
		List<Integer> ids = Lists.newArrayList();
		try {
			
			SwopManager swops = player.getSwops();
			
			for( SwopReslut o : resluts ){
				SpecialSwop swopinfo = swops.getSwopInfo( sid, o.id );
				LuaValue[] value= LuaUtil.getExchangeInfo().getField( "getSpecialExchangeInfo" ).call( 3, sid, o.id );
				int times 		= value[0].getInt();
				int expendId 	= value[1].getInt();
				int expendCount = value[2].getInt();
				if( expendId == 0 )
					continue;
				// 如果为null 那么直接添加
				if( swopinfo == null ){
					swopinfo 	= new SpecialSwop( sid, o.id );
					swops.getSpecials().add(swopinfo);
				}
				// 这里检测是否次数已经完了
				if( swopinfo.getTimes() + o.count > times )
					continue;
				// 这里开始执行扣道具
				List<IProp> update = deductPropAndAddProp( player, sid, o, expendId, expendCount );
				if( update == null || update.isEmpty() )
					continue;
				ret.addAll( update );
				ids.add( o.id );
				// 累加次数
				swopinfo.addTimes( o.count );
			}
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 20 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			buffer.writeByte( ids.size() );
			for( int id : ids )
				buffer.writeInt(id);
			buffer.writeByte( ret.size() );
			for( IProp prop : ret ){
				prop.putBaseBuffer(buffer);
				prop.buildTransformStream(buffer);
			}
		}
		sendPackage( player.getCtx(), buffer );
	}

	/**
	 * 扣除道具 和 添加道具
	 * @param player
	 * @param expendId
	 * @param expendCount
	 * @param expendCount2 
	 * @param expendId2 
	 * @return
	 */
	private List<IProp> deductPropAndAddProp( Player player, int sid, SwopReslut o, int expendId, int expendCount ) {
		StarDepot depot = player.getDepots(sid);
		// 先执行扣除
		if( expendId == LXConstants.CURRENCY_NID ){
			if( player.changeCurrency( -expendCount*o.count, "特殊兑换扣除" ) == -1 )
				return null;
		} else if( !depot.deductPropByNid( expendId, expendCount ) )
			return null;
		// 这里添加道具
		return depot.appendProp( o.id, o.count );
	}

}

class SwopReslut{
	
	int id;
	
	int count;
}