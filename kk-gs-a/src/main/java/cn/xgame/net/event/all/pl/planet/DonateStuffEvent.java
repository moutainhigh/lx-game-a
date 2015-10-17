package cn.xgame.net.event.all.pl.planet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.data.building.UnBuildings;
import cn.xgame.a.world.planet.data.resource.ResourceControl;
import cn.xgame.a.world.planet.data.tech.UnTechs;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.a.world.planet.home.o.Syn;
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
			HomePlanet planet = WorldManager.o.getHomePlanet(id);
			
			// 扣除捐献的道具  uid=-1代表 是捐献货币
			IProp prop = uid == -1 ? deductCurrency(player, count) : deductProp( id, player, uid, count );
			
			// 添加资源
			ResourceControl depotControl = planet.getDepotControl();
			List<IProp> update = depotControl.appendProp(prop);
			
			// 这里检查  建筑中列表 里面是否有材料不足而暂停的建筑中建筑
			List<UnBuildings> waitBuild = planet.getBuildingControl().getWaitBuild();
			for( UnBuildings unBuild : waitBuild ){
				if( depotControl.deductProp( unBuild.templet().needres ) ){
					unBuild.setEndtime( (int) (System.currentTimeMillis()/1000) + unBuild.templet().needtime );
				}
			}
			
			// 检测科技
			List<UnTechs> waitTech = planet.getTech().getWaitTech();
			for( UnTechs unTech : waitTech ){
				if( depotControl.deductProp( unTech.templet().needres ) ){
					unTech.setEndtime( (int) (System.currentTimeMillis()/1000) + unTech.templet().needtime );
				}
			}
			
			// 处理一下捐献后的操作
			planet.handleDonateLater(player, prop);
			
			// 捐献成功后 同步消息
			Syn.res( planet.getPeoples(), update );
			
			Logs.debug( player.getCtx(), "捐献资源  星球ID="+id+",uid="+uid+",count="+count );
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
		if( player.changeCurrency( -count, "给星球捐献货币" ) == -1 )
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
