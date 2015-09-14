package cn.xgame.a.player.fleet.status;

import x.javaplus.util.ErrorCode;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.other.IPurpose;
import cn.xgame.a.player.fleet.other.IStatus;
import cn.xgame.a.player.fleet.other.StatusType;

/**
 * 航行状态
 * @author deng		
 * @date 2015-9-11 上午12:37:43
 */
public class SailStatus extends IStatus{
	
	// 目标星球ID
	private int aimId;
	
	// 航行结束时间
	private int endtime;
	
	// 航行目的
	private IPurpose purpose;
	
	public SailStatus(StatusType type) {
		super(type);
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt(aimId);
		buf.writeInt(endtime);
		buf.writeByte( purpose.type() );
		purpose.putBuffer(buf);
	}
	
	@Override
	public void wrapBuffer(ByteBuf buf) {
		aimId 		= buf.readInt();
		endtime 	= buf.readInt();
		byte type 	= buf.readByte();
		purpose 	= IPurpose.create( type, buf );		
	}

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
//		buffer.writeInt(aimId);
//		buffer.writeInt(endtime);
//		purpose.buildTransformStream(buffer);
	}
	
	@Override
	public boolean canFighting() throws Exception{
//		int t = (int) (System.currentTimeMillis()/1000);
		
		throw new Exception( ErrorCode.SHIP_NOTINSTAR.name() );
	}
	
	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
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

}
