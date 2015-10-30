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
	
	// 航行时间
	private int continutime;
	
	// 等待时间  一般只会出现在组队情况下
	private int waittime;
	
	// 航行目的
	private IPurpose purpose;
	
	public SailStatus() {
		super( StatusType.SAIL );
	}
	
	@Override
	public void init( Object[] objects ) {
		int i = 0;
		aimId 		= (Integer) objects[i++];
		starttime	= (Integer) objects[i++];
		continutime	= (Integer) objects[i++];
		purpose		= (IPurpose) objects[i++];
		waittime	= (Integer) objects[i++];
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeInt(aimId);
		buf.writeInt(starttime);
		buf.writeInt(continutime);
		buf.writeInt(waittime);
		buf.writeByte( purpose.type() );
		purpose.putBuffer(buf);
	}
	
	@Override
	public void wrapBuffer(ByteBuf buf) {
		aimId 		= buf.readInt();
		starttime	= buf.readInt();
		continutime = buf.readInt();
		waittime	= buf.readInt();
		byte type 	= buf.readByte();
		purpose 	= IPurpose.create( type, buf );		
	}

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		super.buildTransformStream(buffer);
		buffer.writeInt(aimId);
		buffer.writeInt( getAlreadySailtime() );
		buffer.writeInt(continutime);
		buffer.writeInt(waittime);
		purpose.buildTransformStream(buffer);
	}

	// 获取已经航行的时间 - 只用到前端显示
	private int getAlreadySailtime() {
		return (int) (System.currentTimeMillis()/1000) - starttime;
	}
	
	@Override
	public boolean isComplete() {
		return (int) (System.currentTimeMillis()/1000) >= (starttime+continutime+waittime);
	}
	
	@Override
	public void update( FleetInfo fleet, Player player ) {
		// 执行航行目的
		purpose.execut( starttime, continutime, aimId, fleet, player );
	}
	
	public int getAimId() {
		return aimId;
	}
	public int getEndtime() {
		return continutime;
	}
	public IPurpose getPurpose() {
		return purpose;
	}
	public int getStarttime() {
		return starttime;
	}
	public int getWaittime() {
		return waittime;
	}

}
