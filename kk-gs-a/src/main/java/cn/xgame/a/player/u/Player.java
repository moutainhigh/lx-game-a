package cn.xgame.a.player.u;

import java.util.List;

import x.javaplus.util.Util.Time;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.captain.CaptainsControl;
import cn.xgame.a.player.depot.DepotControl;
import cn.xgame.a.player.ectype.AccEctypeControl;
import cn.xgame.a.player.ectype.o.AccEctype;
import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.ship.DockControl;
import cn.xgame.a.system.SystemCfg;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.gen.dto.MysqlGen.PlayerDataDto;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.ls.RLastGsidEvent;
import cn.xgame.net.netty.Netty.Attr;
import cn.xgame.net.netty.Netty.IP;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.PackageCheck;

public class Player extends IPlayer implements ITransformStream{

	// 玩家的IP地址
	private String ip;
	
	// 网络通信socket
	private ChannelHandlerContext ctx = null;
	
	// 包检测
	private PackageCheck pcheck = new PackageCheck();

	
	
	//////////////////////数据库相关//////////////////////////
	
	// 所有道具唯一ID基础值 
	private DBBaseUID propBaseUid = new DBBaseUID( this );

	// 背包
	private DepotControl depots = new DepotControl( this );
	
	// 船坞
	private DockControl docks = new DockControl( this );
	
	// 舰长室
	private CaptainsControl captains = new CaptainsControl( this );
	
	// 副本信息
	private AccEctypeControl accEctypes = new AccEctypeControl( this );
	
	/**
	 * 创建一个
	 * @param uID
	 * @param headIco
	 * @param name
	 */
	public Player( String uID, int headIco, String name ) {
		setUID( uID );
		setGsid( SystemCfg.ID );
		setHeadIco( headIco );
		setNickname( name );
		setCreateTime( System.currentTimeMillis() );
		setManors( new ManorControl() );
		// 获取偶发副本
		updateAccEctype();
	}
	
	/**
	 * 从数据库获取一个
	 * @param dto
	 */
	public Player( PlayerDataDto dto ) {
		wrap( dto );
		// 取出偶发副本信息
		if( dto.getAccEctypes() == null )
			updateAccEctype();
		else
			accEctypes.fromBytes( dto.getAccEctypes() );
		// 取出所有道具类型的基础UID
		propBaseUid.fromDB();
		// 在数据库取出 玩家的所有道具
		depots.fromDB();
		// 在数据库取出 玩家舰船数据
		docks.fromDB();
		// 在数据库取出 玩家舰长数据
		captains.fromDB();
	}

	@Override
	public void update( PlayerDataDto dto ) {
		super.update(dto);
		// 偶发副本
		dto.setAccEctypes( accEctypes.toBytes() );
	}
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		
		RW.writeString( buffer, getUID() );
		RW.writeString( buffer, getNickname() );
		buffer.writeInt( getHeadIco() );
		buffer.writeInt( getAdjutantId() );
		buffer.writeInt( getCurrency() );
		buffer.writeInt( getGold() );
		buffer.writeByte( 1 );
	}
	

	/**  退出处理 */
	public void exit() {
		setLastLogoutTime( System.currentTimeMillis() );
		
		// 保存所有道具 信息
		depots.update();
		// 保存所有舰船 信息
		docks.update();
		// 保存所有舰长 信息
		captains.update();
	}


	/**
	 * 获取 跨天 天数 
	 * @return -1.表示 没有过天   >=0.表示已经过的天数
	 */
	public int strideDay() {
		long t = System.currentTimeMillis() - Time.refTimeInMillis( getLastLogoutTime(), 24, 0, 0 );
		return t >= 0 ? (int) (t / 86400000l) : -1;
	}
	
	/** 记录 最后一次登录的服务器ID */
	public void rLastGsid() {
		((RLastGsidEvent)Events.RLAST_GSID.getEventInstance()).run( getUID() );
	}

	
	/** 更新一下偶发副本 */
	public void updateAccEctype(){
		accEctypes.clear();
		List<IPlanet> ls = WorldManager.o.getAllPlanet();
		for( IPlanet o : ls ){
			List<AccEctype> v = o.getAccEctype();
			if( v == null ) continue;
			accEctypes.append( v );
		}
	}
	
	public int generatorPropUID() {
		return propBaseUid.generatorPropUID();
	}
	public int generatorShipUID() {
		return propBaseUid.generatorShipUID();
	}
	public int generatorCaptainUID() {
		return propBaseUid.generatorCaptainUID();
	}
	
	/**
	 * 玩家是否在线
	 * @return
	 */
	public boolean isOnline() {
		return ctx != null && Attr.getAttachment(ctx) != null;
	}
	
	
	public String toString(){
		return "UID=" + getUID() + ", nickname=" + getNickname();
	}

	public boolean safeCheck( Events event ) {
		return pcheck.safeCheck( event );
	}
	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	public void setCtx( ChannelHandlerContext ctx ) {
		if( this.ctx != null ){
			Attr.setAttachment( this.ctx, null );
			this.ctx.close();
			this.ctx = null;
		}
		this.ctx = ctx;
		Attr.setAttachment( this.ctx, getUID() );
		ip = IP.formAddress( this.ctx );
	}
	public String getIp() {
		return ip;
	}
	public DBBaseUID getPropBaseUid() {
		return propBaseUid;
	}
	public DepotControl getDepots() {
		return depots;
	}
	public DockControl getDocks() {
		return docks;
	}
	public CaptainsControl getCaptains() {
		return captains;
	}
	public AccEctypeControl getAccEctypes() {
		return accEctypes;
	}




}
