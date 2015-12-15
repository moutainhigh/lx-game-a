package cn.xgame.a.player.u;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
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
import cn.xgame.a.player.mail.MailControl;
import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.swop.SwopManager;
import cn.xgame.a.player.task.TaskControl;
import cn.xgame.a.player.tavern.TavernControl;
import cn.xgame.a.player.u.classes.DBBaseUID;
import cn.xgame.a.player.u.classes.IPlayer;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.PalyerlevelPo;
import cn.xgame.gen.dto.MysqlGen.PlayerDataDto;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.ls.RLastGsidEvent;
import cn.xgame.system.LXConstants;
import cn.xgame.system.SystemCfg;
import cn.xgame.utils.Logs;
import cn.xgame.utils.PackageCheck;
import cn.xgame.net.netty.Netty.Attr;
import cn.xgame.net.netty.Netty.IP;
import cn.xgame.net.netty.Netty.RW;

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
	
	// 玩家群聊频道列表&私聊
	private ChatAxnControl		chats 			= new ChatAxnControl( this );
	
	// 酒馆数据
	private TavernControl 		taverns			= new TavernControl( this );
	
	// 舰队
	private FleetControl		fleets			= new FleetControl( this );
	
	// 任务
	private TaskControl			tasks			= new TaskControl( this );
	
	// 兑换
	private SwopManager			swops			= new SwopManager( this );
	
	//////////////////////////////////////////////////////////////////////
	// 所有道具唯一ID基础值 
	private DBBaseUID 			propBaseUid 	= new DBBaseUID( this );
	
	// 背包
	private DepotControl 		depots 			= new DepotControl( this );
	
	// 船坞
	private DockControl 		docks 			= new DockControl( this );
	
	// 邮件
	private MailControl			mails			= new MailControl( this );
	
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
		// 初始化副本信息
		ectypes.clear();
	}
	
	public String toString(){
		return "UID=" + getUID() + ", nickname=" + getNickname();
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
		chats.fromBytes( dto.getChatAxns() );
		// 酒馆数据
		taverns.fromBytes( dto.getTaverns() );
		// 任务
		tasks.fromBytes( dto.getTasks() );
		// 兑换信息
		swops.fromBytes( dto.getSwops() );
		
		////-------------------------下面不是玩家数据库的  但是需要在获取玩家的时候一起取出来
		// 取出所有道具类型的基础UID
		propBaseUid.fromDB();
		// 在数据库取出 玩家的所有道具
		depots.fromDB();
		// 在数据库取出 玩家船坞数据
		docks.fromDB();
		// 舰队数据 这个必须要取出全部舰船才能调用
		fleets.fromBytes( dto.getFleets() );
		// 邮件
		mails.fromDB();
	}

	@Override
	public void update( PlayerDataDto dto ) {
		super.update(dto);
		// 领地
		dto.setManors( manors.toBytes() );
		// 副本
		dto.setEctypes( ectypes.toBytes() );
		// 聊天
		dto.setChatAxns( chats.toBytes() );
		// 酒馆
		dto.setTaverns( taverns.toBytes() );
		// 舰队
		dto.setFleets( fleets.toBytes() );
		// 任务
		dto.setTasks( tasks.toBytes() );
		// 兑换
		dto.setSwops( swops.toBytes() );
	}
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		RW.writeString( buffer, getUID() );
		RW.writeString( buffer, getNickname() );
		buffer.writeInt( getHeadIco() );
		buffer.writeShort( getLevel() );
		buffer.writeInt( getExp() );
		buffer.writeInt( getAdjutantId() );
		buffer.writeInt( getCurrency() );
		buffer.writeInt( getGold() );
		buffer.writeByte( 1 );
		buffer.writeInt( manors.getNid() );
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
	
	/**  退出处理 */
	public void exit() {
		
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
		return t >= 0 ? (int) (t / Time.DAY_OF_MS) : -1;
	}
	
	/** 记录 最后一次登录的服务器ID */
	public void rLastGsid() {
		((RLastGsidEvent)Events.RLAST_GSID.toInstance()).run( getUID() );
	}

	/**
	 * 改变货币
	 * @param value 添加用正号  减少用负号
	 * @return 返回当前货币 -1表示货币不足
	 */
	public int changeCurrency( int value, String explain ) {
		if( currency + value < 0  )
			return -1;
		currency += value;
		
		Logs.debug( ctx, "货币改变 " + (currency - value) + (value>=0?" + ":" - ") + Math.abs(value) + " = " + currency + " " + explain );
		return currency;
	}
	
	/**
	 * 一个公共扣除资源函数
	 * @param needres
	 * @throws Exception 
	 */
	public List<IProp> deductResource( int sind, String needres, String explain ) throws Exception {
		int needMoney 			= 0;
		List<IProp> materials 	= Lists.newArrayList();
		StarDepot depots 		= getDepots(sind);
		// 先判断是够足够
		String[] array 		= needres.split( "\\|" );
		for( String str : array ){
			String[] res 	= str.split(";");
			int id 			= Integer.parseInt( res[0] );
			int count 		= Integer.parseInt( res[1] );
			if( id == LXConstants.CURRENCY_NID ){
				if( count > getCurrency() )
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
		changeCurrency( -needMoney, explain+"扣除" );
		for( IProp prop : materials ){
			if( prop.isEmpty() )
				depots.remove( prop );
			else
				depots.getProp( prop ).setCount( prop.getCount() );
		}
		Logs.debug( ctx, explain+" 扣除资源 " + materials );
		return materials;
	}
	public List<IProp> deductResource( String needres, String explain ) throws Exception{
		return deductResource( getCountryId(), needres, explain );
	}
	
	/**
	 * 添加经验
	 * @param i
	 */
	public void addExp( int addexp ) {
		this.exp += addexp;
		short curLevel = this.level;
		// 下面刷新升级
		PalyerlevelPo temp = CsvGen.getPalyerlevelPo(level);
		while ( this.exp >= temp.exp && temp.exp != 0 ){
			temp = CsvGen.getPalyerlevelPo(level+1);
			if( temp == null )
				break;
			++this.level;
		}
		// 如果升级了
		if( this.level - curLevel > 0 ){
			Logs.debug(ctx, "升级了 +"+(this.level-curLevel)+" cur="+this.level );
			// 刷新可接任务
			updateCanTask();
			
			
		}
	}
	private void updateCanTask() {
//		List<Integer> ret = Lists.newArrayList();
//		
//		
//		tasks.addCanTask( ret );
//		// 通知
//		((Update_1400)Events.UPDATE_1400.toInstance()).run( this, ret );
	}

	/**
	 * 是否VIP
	 * @return
	 */
	public boolean isVip() {
		return false;
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
	public DepotControl getAllDepots() {
		return depots;
	}
	public StarDepot getDepots( int snid ) {
		return depots.getDepot(snid);
	}
	public StarDepot getDepots() {
		return depots.getDepot(getCountryId());
	}
	public DockControl getDocks() {
		return docks;
	}
	public EctypeControl getEctypes() {
		return ectypes;
	}
	public ChatAxnControl getChatAxns() {
		return chats;
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
	public MailControl getMails() {
		return mails;
	}
	public TaskControl getTasks() {
		return tasks;
	}
	public SwopManager getSwops() {
		return swops;
	}
	
}
