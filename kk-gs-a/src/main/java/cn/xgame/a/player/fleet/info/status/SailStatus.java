package cn.xgame.a.player.fleet.info.status;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.classes.IPurpose;
import cn.xgame.a.player.fleet.classes.IStatus;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;

/**
 * 航行状态
 * @author deng		
 * @date 2015-9-11 上午12:37:43
 */
public class SailStatus extends IStatus{
	
	// 目标星球ID
	private int aimId;
	
	// 起始时间 - 组要作用给前端展示
	private int starttime;
	
	// 航行结束时间
	private int endtime;
	
	// 航行目的
	private IPurpose purpose;
	
	public SailStatus() {
		super( StatusType.SAIL );
	}
	
	@Override
	public void init( Object[] objects ) {
		aimId 		= (Integer) objects[0];
		starttime	= (Integer) objects[1];
		endtime		= (Integer) objects[2];
		purpose		= (IPurpose) objects[3];
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt(aimId);
		buf.writeInt(starttime);
		buf.writeInt(endtime);
		buf.writeByte( purpose.type() );
		purpose.putBuffer(buf);
	}
	
	@Override
	public void wrapBuffer(ByteBuf buf) {
		aimId 		= buf.readInt();
		starttime	= buf.readInt();
		endtime 	= buf.readInt();
		byte type 	= buf.readByte();
		purpose 	= IPurpose.create( type, buf );		
	}

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		super.buildTransformStream(buffer);
		buffer.writeInt(aimId);
		buffer.writeInt(starttime);
		buffer.writeInt(endtime);
		purpose.buildTransformStream(buffer);
	}
	
	@Override
	public boolean isComplete() {
		return (int) (System.currentTimeMillis()/1000) >= endtime;
	}
	
	@Override
	public void update( FleetInfo fleet, Player player ) {
		// 执行航行目的
		purpose.execut( endtime, aimId, fleet, player );
	}
	
	public int getAimId() {
		return aimId;
	}
	public void setAimId(int aimId) {
		this.aimId = aimId;
	}
	public int getEndtime() {
		return endtime;
	}
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	public IPurpose getPurpose() {
		return purpose;
	}
	public void setPurpose(IPurpose purpose) {
		this.purpose = purpose;
	}
	public int getStarttime() {
		return starttime;
	}
	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}

}
