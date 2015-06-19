package cn.xgame.a.player.u;

import x.javaplus.ip.IPSeeker;
import x.javaplus.util.Util.Time;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import cn.xgame.a.player.ITransformStream;
import cn.xgame.a.player.prop.PropBaseUID;
import cn.xgame.a.player.prop.PropControl;
import cn.xgame.a.player.prop.PropType;
import cn.xgame.a.system.SystemCfg;
import cn.xgame.gen.dto.MysqlGen.PlayerDataDto;
import cn.xgame.net.event.Events;
import cn.xgame.net.netty.Netty.Attr;
import cn.xgame.net.netty.Netty.IP;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.utils.PackageCheck;

public class Player extends IPlayer implements ITransformStream{

	// 网络通信socket
	private ChannelHandlerContext ctx = null;
	
	// 包检测
	private PackageCheck pcheck = new PackageCheck();

	// 背包
	private PropControl props = new PropControl( this );
	
	// 所有道具唯一ID基础值 
	private PropBaseUID propBaseUid = new PropBaseUID( this );
	
	/**
	 * 创建一个
	 * @param uID
	 * @param headIco
	 * @param name
	 */
	public Player( String uID, int headIco, String name ) {
		setUID(uID);
		setGsid( SystemCfg.ID );
		setHeadIco(headIco);
		setNickname(name);
		setCreateTime( System.currentTimeMillis() );
	}
	
	/**
	 * 从数据库获取一个
	 * @param dto
	 */
	public Player( PlayerDataDto dto ) {
		wrap( dto );
		// 在数据库取出 玩家的所有道具
		props.fromDB();
		// 取出所有道具类型的基础UID
		propBaseUid.fromDB();
	}

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		
		RW.writeString( buffer, getUID() );
		RW.writeString( buffer, getNickname() );
		buffer.writeInt( getHeadIco() );
		RW.writeString( buffer, getCountry() ) ;
		buffer.writeInt( getCurrency() );
		buffer.writeInt( getGold() );
	}
	
	
	

	/**  退出处理 */
	public void exit() {
		setLastLogoutTime( System.currentTimeMillis() );
	}


	/** 获取 跨天 天数 */
	public int strideDay() {
		long t = System.currentTimeMillis() - Time.refTimeInMillis( getLastLogoutTime(), 24, 0, 0 );
		return (int) (t / 86400000l);
	}


	/**
	 * 生成对应道具 唯一ID
	 * @param type  
	 * @return
	 */
	public int generatorPropUID( PropType type ) {
		return propBaseUid.generatorUID( type );
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
		String country = IPSeeker.I.getCountry( IP.formAddress(ctx) );
		setCountry( country );
		this.ctx = ctx;
		Attr.setAttachment( this.ctx, getUID() );
	}
	public PropBaseUID getPropBaseUid() {
		return propBaseUid;
	}
	public PropControl getProps() {
		return props;
	}




}