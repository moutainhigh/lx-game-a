package cn.xgame.a.player.fleet.o;

import io.netty.buffer.ByteBuf;

import java.util.Iterator;
import java.util.List;

import cn.xgame.a.ITransformStream;
import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.player.dock.ship.ShipInfo;
import cn.xgame.a.player.fleet.other.IPurpose;
import cn.xgame.a.player.fleet.other.IStatus;
import cn.xgame.a.player.fleet.other.StatusType;
import cn.xgame.a.player.fleet.status.CombatStatus;
import cn.xgame.a.player.fleet.status.SailStatus;
import cn.xgame.a.player.u.Player;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;

/**
 * 一个舰队信息
 * @author deng		
 * @date 2015-9-10 下午10:06:18
 */
public class FleetInfo implements ITransformStream{
	
	// 舰船列表
	private List<ShipInfo> 	ships 			= Lists.newArrayList();
	
	// 舰队状态
	private IStatus 		status 			= StatusType.LEISURE.create();
	
	// 当前停靠星球ID
	private int 			berthSnid		= -1;
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeByte( ships.size() );
		for( ShipInfo ship : ships )
			buffer.writeInt( ship.getuId() );
		buffer.writeInt( berthSnid );
		status.buildTransformStream(buffer);
	}
	
	public List<ShipInfo> getShips() { return ships; }
	public IStatus getStatus() { return status; }
	public void setStatus( IStatus status ){ this.status = status; }
	public int getBerthSnid() { return berthSnid; }
	
	public void setBerthSnid( int berthSnid ) { 
		this.berthSnid = berthSnid;
		for( ShipInfo ship : ships )
			ship.setBerthSid(berthSnid);
	}
	
	public ShipInfo getShip( int suid ){
		for( ShipInfo ship : ships ){
			if( ship.getuId() == suid )
				return ship;
		}
		return null;
	}
	
	/**
	 * 是否空闲状态
	 * @return
	 * @throws Exception
	 */
	public boolean isLeisure() throws Exception {
		if( status.type() == StatusType.SAIL || status.type() == StatusType.COMBAT )
			throw new Exception( ErrorCode.SHIP_NOTLEISURE.name() );
		return true;
	}
	
	/**
	 * 添加一个舰船到这个舰队
	 * @param ship
	 */
	public void add( ShipInfo ship ) {
		if( getShip(ship.getuId()) != null )
			return;
		ships.add(ship);
		// 如果还是空闲状态 那么设置为悬停
		if( status.type() == StatusType.LEISURE ){
			status 		= StatusType.HOVER.create();
			berthSnid 	= ship.getBerthSid();
		}
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
		// 如果没有舰船在这个舰队了上了 那么就要设置为空闲状态
		if( ships.isEmpty() ){
			status 		= StatusType.LEISURE.create();
			berthSnid 	= -1;
		}
	}
	
	/**
	 * 执行状态 一般在登录的时候调用
	 * @param player
	 */
	public void executeStatus( Player player ) {
		if( !status.isComplete() ) return;
		status = status.execut( this, player );
	}
	
	/**
	 * 切换为航行状态
	 * @param aimId
	 * @param stime
	 * @param purpose 
	 */
	public IStatus changeSail( int aimId, int stime, IPurpose purpose ) {
		SailStatus o = new SailStatus();
		o.setAimId(aimId);
		o.setEndtime( (int)(System.currentTimeMillis()/1000) + stime );
		o.setPurpose( purpose );
		status = o;
		return status;
	}

	/**
	 * 切换战斗状态
	 * @param snid 
	 * @param type
	 * @param cnid
	 * @param enid
	 * @param ctime
	 * @param iswin
	 * @param awards
	 * @param score 
	 */
	public IStatus changeCombat( int snid, byte type, int cnid, int enid, int ctime, byte iswin, List<AwardInfo> awards, int score) {
		CombatStatus o = new CombatStatus();
		o.setType( type );
		o.setChapterId( cnid );
		o.setEctypeId( enid );
		o.setEndtime( (int)(System.currentTimeMillis()/1000) + ctime );
		o.setCtime( ctime );
		o.setIsWin( iswin );
		if( awards != null )
			o.getAwards().addAll( awards );
		o.setScore( score );
		// 最后设置状态
		status 		= o;
		// 这里还要设置停靠星球ID
		setBerthSnid( snid );
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

}
