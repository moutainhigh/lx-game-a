package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.net.event.IEvent;
import cn.xgame.system.LXConstants;
import cn.xgame.utils.Logs;

/**
 * 捐献资源 - 材料
 * @author deng		
 * @date 2015-7-1 上午10:24:48
 */
public class DonateStuffEvent extends IEvent{

	@Override
	public void run( Player player, ByteBuf data ) throws IOException {
		
		int id			= data.readInt();
		int uid 		= data.readInt();
		int count		= data.readInt();
		
		ErrorCode code = null;
		
		try {
			if( count <= 0 )
				throw new Exception( ErrorCode.STUFF_LAZYWEIGHT.name() );
			
			IPlanet planet = WorldManager.o.getPlanet( id );
			if( planet == null )
				throw new Exception( ErrorCode.PLANET_NOTEXIST.name() );
			if( !planet.isCanDonate() )
				throw new Exception( ErrorCode.CANNOT_DONATE.name() );
			
			// 扣除捐献的道具  uid=-1代表 是捐献货币
			IProp prop = uid == -1 ? deductCurrency(player, count) : deductProp( id, player, uid, count );
			
			// 开始捐献
			planet.donateResource( player, prop );
			
			Logs.debug( player, "捐献资源  星球ID="+id+",uid="+uid+",count="+count );
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		// 返回客户端
		ByteBuf response = buildEmptyPackage( player.getCtx(), 2 );
		response.writeShort( code.toNumber() );
		sendPackage( player.getCtx(), response );
		
	}

	
	
	
	
	// 扣除货币
	private IProp deductCurrency(Player player, int count) throws Exception {
		if( player.changeCurrency( -count ) == -1 )
			throw new Exception( ErrorCode.CURRENCY_LAZYWEIGHT.name() );
		return IProp.create( 0, LXConstants.CURRENCY_NID, count );
	}

	// 扣除道具
	private IProp deductProp( int id, Player player, int uid, int count) throws Exception {
		
		StarDepot depot = player.getDepots(id);
		IProp prop = depot.getProp( uid );
		if( prop == null )
			throw new Exception( ErrorCode.STUFF_NOTEXIST.name() );
		
		if( prop.getCount() < count )
			throw new Exception( ErrorCode.STUFF_LAZYWEIGHT.name() );
		
		// 设置数量
		prop = prop.clone();
		prop.setCount( count );
		
		// 扣除 玩家身上的道具  注：这里要先扣除玩家身上的道具 因为下面捐献后 会把UID换掉
		depot.deductProp( prop );
		
		return prop;
	}

}
