package cn.xgame.a.player.u;

import x.javaplus.util.Util.Time;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.chat.ChatAxnControl;
import cn.xgame.a.player.depot.DepotControl;
import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.dock.DockControl;
import cn.xgame.a.player.ectype.EctypeControl;
import cn.xgame.a.player.fleet.FleetControl;
import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.tavern.TavernControl;
import cn.xgame.a.player.u.o.DBBaseUID;
import cn.xgame.a.player.u.o.IPlayer;
import cn.xgame.gen.dto.MysqlGen.PlayerDataDto;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.ls.RLastGsidEvent;
import cn.xgame.net.netty.Netty.Attr;
import cn.xgame.net.netty.Netty.IP;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.system.SystemCfg;
import cn.xgame.utils.PackageCheck;

public class Player extends IPlayer implements ITransformStream{

	//////////////////////临时数据 不用保存数据库//////////////////////////
	
	// 玩家的IP地址
	private String ip;
	
	// 网络通信socket
	private ChannelHandlerContext ctx 	= null;
	
	// 包检测
	private PackageCheck pcheck 		= new PackageCheck();
	
	
	//////////////////////数据库相关//////////////////////////
	
	// 玩家领地
	private ManorControl 		manors			= new ManorControl( this );
	
	// 副本操作
	private EctypeControl 		ectypes 		= new EctypeControl( this );
	
	// 玩家聊天聊天频道列表
	private ChatAxnControl		chatAxns 		= new ChatAxnControl( this );
	
	// 酒馆数据
	private TavernControl 		taverns			= new TavernControl( this );
	
	// 舰队
	private FleetControl		fleets			= new FleetControl( this );
	
	
	//////////////////////////////////////////////////////////////////////
	// 所有道具唯一ID基础值 
	private DBBaseUID 			propBaseUid 	= new DBBaseUID( this );
	
	// 背包
	private DepotControl 		depots 			= new DepotControl( this );
	
	// 船坞
	private DockControl 		docks 			= new DockControl( this );
	
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
		taverns.init();
		// 初始化副本信息
		ectypes.initialize();
	}
	
	/**
	 * 从数据库获取一个
	 * @param dto
	 */
	public Player( PlayerDataDto dto ) {
		wrap( dto );
		// 取出 领地
		manors.fromBytes( dto.getManors() );
		// 副本信息
		ectypes.fromBytes( dto.getEctypes() );
		// 取出 聊天频道列表
		chatAxns.fromBytes( dto.getChatAxns() );
		// 酒馆数据
		taverns.fromBytes( dto.getTaverns() );
		
		////-------------------------下面不是玩家数据库的  但是需要在获取玩家的时候一起取出来
		// 取出所有道具类型的基础UID
		propBaseUid.fromDB();
		// 在数据库取出 玩家的所有道具
		depots.fromDB();
		// 在数据库取出 玩家船坞数据
		docks.fromDB();
		// 舰队数据 这个必须要取出全部舰船才能调用
		fleets.fromBytes( dto.getFleets() );
	}

	@Override
	public void update( PlayerDataDto dto ) {
		super.update(dto);
		// 领地
		dto.setManors( manors.toBytes() );
		// 副本
		dto.setEctypes( ectypes.toBytes() );
		// 聊天
		dto.setChatAxns( chatAxns.toBytes() );
		// 酒馆
		dto.setTaverns( taverns.toBytes() );
		// 舰队
		dto.setFleets( fleets.toBytes() );
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
		
		// 保存所有道具 信息 这里不保存 让道具及时保存
//		depots.update();
		// 保存所有舰船 信息
		docks.update();
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
		return ctx != null  && ctx.channel().isActive() && Attr.getAttachment(ctx) != null;
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
	public StarDepot getDepots( int snid ) {
		return depots.getDepot(snid);
	}
	public DockControl getDocks() {
		return docks;
	}
	public EctypeControl getEctypes() {
		return ectypes;
	}
	public ChatAxnControl getChatAxns() {
		return chatAxns;
	}
	public ManorControl getManors() {
		return manors;
	}
	public TavernControl getTaverns() {
		return taverns;
	}
	public FleetControl getFleets() {
		return fleets;
	}
	
}
