package cn.xgame.a.player.ship.o.status;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;

/**
 * 状态操作中心
 * @author deng		
 * @date 2015-7-31 上午2:20:48
 */
public class StatusControl implements IArrayStream, ITransformStream{

	//状态
	private ShipStatus status 	= ShipStatus.LEVITATION;
	
	// 当前星球ID
	private int currentSnid		= -1;
	
	// 目标星球ID
	private int targetSnid 		= 0;
	
	// 航行目的
	private SailPurpose	sailPurpose = null;
	
	// 记录时间 单位秒
	private int rTime 			= -1;
	// 持续时间
	private int durationTime 	= -1;
	
	/**
	 * 初始化 状态
	 * @param countryId
	 */
	public void init( int countryId ) {
		this.status 		= ShipStatus.LEVITATION;
		this.currentSnid	= countryId;
	}
	
	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		status 		= ShipStatus.fromNum( buf.readByte() );
		currentSnid	= buf.readInt();
		if( status == ShipStatus.SAILING ){
			targetSnid 		= buf.readInt();
			sailPurpose 	= SailPurpose.fromNum( buf.readByte() );
		}
		if( status == ShipStatus.COMBAT || status == ShipStatus.SAILING ){
			rTime			= buf.readInt();
			durationTime	= buf.readInt();
		}
	}

	@Override
	public byte[] toBytes() {
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeByte( status.toNumber() );
		buf.writeInt( currentSnid );
		if( status == ShipStatus.SAILING ){
			buf.writeInt( targetSnid );
			buf.writeByte( sailPurpose.toNumber() );
		}
		if( status == ShipStatus.COMBAT || status == ShipStatus.SAILING ){
			buf.writeInt( rTime );
			buf.writeInt( durationTime );
		}
		return buf.array();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( status.toNumber() );
		buffer.writeInt( currentSnid );
	}
	
	/**
	 * 获取剩余时间
	 * @return
	 */
	public int getSurplusTime() {
		if( rTime == -1 )
			return 0;
		int t 	= (int) (System.currentTimeMillis()/1000 - rTime);
		int ret = durationTime - t;
		return ret < 0 ? 0 : ret;
	}

	
	/**
	 * 悬停
	 */
	public void levitation() {
		status			= ShipStatus.LEVITATION;
		currentSnid 	= targetSnid != 0 ? targetSnid : currentSnid;
		rTime 			= -1;
		durationTime 	= -1;
		targetSnid		= 0;
		sailPurpose 	= null;
	}
	
	/**
	 * 开始航行
	 * @param snid 目标星球
	 * @param sailTime 航行时间
	 * @param purpose 
	 */
	public void startSail(int snid, int sailTime, SailPurpose purpose) {
		status			= ShipStatus.SAILING;
		targetSnid 		= snid;
		rTime			= (int) (System.currentTimeMillis()/1000);
		durationTime	= sailTime;
		sailPurpose		= purpose;
	}
	
	/**
	 * 开始战斗
	 * @param combatTime
	 */
	public void startAttack(int combatTime) {
		status			= ShipStatus.COMBAT;
		rTime			= (int) (System.currentTimeMillis()/1000);
		durationTime	= combatTime;
	}

	
	/**
	 * 是否战斗相关
	 * @return
	 */
	public boolean isCombat() {
		return status == ShipStatus.COMBAT;
	}
	
	public ShipStatus getStatus() {
		return status;
	}
	public void setStatus(ShipStatus status) {
		this.status = status;
	}
	public int getrTime() {
		return rTime;
	}
	public void setrTime(int rTime) {
		this.rTime = rTime;
	}
	public int getDurationTime() {
		return durationTime;
	}
	public void setDurationTime(int durationTime) {
		this.durationTime = durationTime;
	}
	public int getCurrentSnid() {
		return currentSnid;
	}
	public void setCurrentSnid(int lastSnid) {
		this.currentSnid = lastSnid;
	}
	public int getTargetSnid() {
		return targetSnid;
	}
	public void setTargetSnid(int targetSnid) {
		this.targetSnid = targetSnid;
	}
	public void setSailPurpose(SailPurpose sailPurpose) {
		this.sailPurpose = sailPurpose;
	}
	public SailPurpose getSailPurpose(){
		return sailPurpose;
	}




}
