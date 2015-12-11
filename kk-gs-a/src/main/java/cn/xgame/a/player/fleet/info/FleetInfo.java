package cn.xgame.a.player.fleet.info;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.axn.classes.IAxnCrew;
import cn.xgame.a.chat.axn.info.AxnInfo;
import cn.xgame.a.fighter.DamagedInfo;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.fighter.ShipData;
import cn.xgame.a.player.dock.DockControl;
import cn.xgame.a.player.dock.capt.CaptainInfo;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.fleet.classes.IStatus;
import cn.xgame.a.player.fleet.classes.LotteryInfo;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.status.CombatStatus;
import cn.xgame.a.player.u.Player;

/**
 * 一个舰队信息
 * @author deng		
 * @date 2015-9-10 下午10:06:18
 */
public class FleetInfo{
	
	// 舰队编号
	private byte			No ;
	
	// 舰队停靠星球ID
	private int				berthSnid;
	
	// 舰船列表
	private List<ShipInfo> 	ships 			= Lists.newArrayList();
	
	// 舰队状态
	private IStatus 		status ;
	
	// 组队频道ID
	private int 			axnId			= -1;
	
	
	// 临时记录 舰队打完副本是否可以抽奖
	private List<LotteryInfo> lotterys		= Lists.newArrayList();
	
	public FleetInfo( byte No ){
		this.No = No;
	}
	
	public void buildTransformStream( Player player, ByteBuf buffer ) {
		buffer.writeByte( No );
		buffer.writeInt( berthSnid );
		buffer.writeInt( axnId );
		AxnInfo axnInfo = ChatManager.o.axns().getAXNInfo(axnId);
		buffer.writeByte( axnInfo == null ? 0 : (axnInfo.getAxnCrews().size()-1) );
		if( axnInfo != null ){
			List<IAxnCrew> crews = axnInfo.getAxnCrews();
			for( IAxnCrew crew : crews ){
				if( crew.getUid().equals( player.getUID() ) )
					continue;
				crew.putBuffer(buffer);
			}
		}
		buffer.writeByte( ships.size() );
		for( ShipInfo ship : ships )
			buffer.writeInt( ship.getuId() );
		status.buildTransformStream(buffer);
	}
	
	public List<ShipInfo> getShips() { return ships; }
	public IStatus getStatus() { return status; }
	public void setStatus( IStatus status ){ this.status = status; }
	public int getAxnId() { return axnId; }
	public void setAxnId(int axnId) { this.axnId = axnId; }
	public byte getNo() { return No; }
	
	public void setBerthSnid( int berthSnid ) { 
		this.berthSnid = berthSnid;
		for( ShipInfo ship : ships )
			ship.setBerthSid(berthSnid);
	}
	public int getBerthSnid() {
		return berthSnid;
	}
	public List<LotteryInfo> getLotterys() {
		return lotterys;
	}
	
	public ShipInfo getShip( int suid ){
		for( ShipInfo ship : ships ){
			if( ship.getuId() == suid )
				return ship;
		}
		return null;
	}
	
	/**
	 * 添加一个舰船到这个舰队
	 * @param ship
	 */
	public void addShip( ShipInfo ship ) {
		ships.add( ship );
	}
	
	/**
	 * 从这个舰队删除一个舰船
	 * @param ship
	 */
	public void remove( ShipInfo ship ) {
		Iterator<ShipInfo> iter = ships.iterator();
		while (iter.hasNext()) {
			if( iter.next().getuId() == ship.getuId() ) {
				iter.remove();
				break;
			}
		}
	}
	public void removeAll() {
		ships.clear();
	}
	
	/**
	 * 舰队是否空
	 */
	public boolean isEmpty(){
		return ships.isEmpty();
	}
	
	public boolean isHover() {
		return status.type() == StatusType.HOVER;
	}
	public boolean isSail() {
		return status.type() == StatusType.SAIL;
	}
	public boolean isCombat() {
		return status.type() == StatusType.COMBAT;
	}
	public boolean isVote() {
		return status.type() == StatusType.VOTE;
	}

	/**
	 * 获取当前耐久和总耐久
	 * @return
	 */
	public int[] getAllDur() {
		int[] ret = new int[2];
		for( ShipInfo ship : ships ){
			ret[0] += ship.getCurrentHp();
			ret[1] += ship.attr().getMaxHp();
		}
		return ret;
	}
	
	/**
	 * 刷新状态结果  - 一般在登录的时候调用
	 * @param player
	 */
	public void updateStatus( Player player ) {
		lotterys.clear();
		if( !status.isComplete() ) 
			return;
		status.update( this, player );
	}
	
	/**
	 * 切换状态
	 * @param type
	 * @param objects
	 */
	public IStatus changeStatus( StatusType type, Object ...objects ) {
		status = type.create( );
		status.init( objects );
		return status;
	}
	
	/**
	 * 返回一个战斗者
	 * @return
	 */
	public Fighter fighter(){
		Fighter fighter = new Fighter();
		for( ShipInfo ship : ships ){
			fighter.hp += ship.getCurrentHp();
			ship.wrapAttackattr( fighter );
		}
		return fighter;
	}

	/**
	 * 计算战损
	 * @param root
	 * @return
	 */
	public DamagedInfo computeDamage( Player root ) {
		DamagedInfo ret = new DamagedInfo();
		if( !isCombat() ) return ret;
		CombatStatus temp = (CombatStatus) status;
		
		List<ShipInfo> removes = Lists.newArrayList();
		
		DockControl docks = root.getDocks();
		for( ShipInfo ship : ships ){
			// 弹药消耗
//			if( Random.get( 0, 10000 ) <= temp.getAmmoExpend() )
//				ship.toreduceAmmo( -1 );
			// 舰长忠诚度
			CaptainInfo capt = docks.getCaptain( ship.getCaptainUID() );
			if( capt != null ){
				if( capt.changeLoyalty( temp.getIsWin() == 1 ? 1 : -1 ) ){
					docks.destroyCapt( capt );
					ship.setCaptainUID( -1 );
				}
				ret.addLossCapt( ship, capt );
			}
			// 获取所有装备 精密度 + 船本身精密度
			int accuracy = ship.attr().getAccuracy();
			accuracy = accuracy <= 0 ? 1 : accuracy;
			int allAccuracy = ship.allEctypeAccuracy() + accuracy;
			float scale = ((float)temp.getDamaged())/((float)allAccuracy);
			if( scale == 0 )
				continue;
			// 舰船本身战损
			ship.addCurrentHp( -(int) (accuracy * scale) );
			if( ship.getCurrentHp() < ship.attr().getMaxHp() ){
				ret.addLossShip( ship );
				if( ship.getCurrentHp() == 0 ) //如果血量为0  那么就会报废
					removes.add(ship);
			}
			// 装备战损
			ship.computeEquipDamage( ret, scale );
		}
		
		ships.removeAll(removes);
		return ret;
	}

	public List<ShipData> toShipDatas() {
		List<ShipData> ret = new ArrayList<ShipData>();
		for( ShipInfo ship : ships ){
			ShipData x = new ShipData();
			x.push = ship.getPush();
			x.pushratio = ship.attr().templet().pushratio;
			x.mess = ship.allMess();
			ret.add(x);
		}
		return ret;
	}

}
