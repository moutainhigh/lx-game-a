package cn.xgame.a.player.fleet.info.status;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.classes.IStatus;
import cn.xgame.a.player.fleet.classes.StatusType;
import cn.xgame.a.player.fleet.classes.IVoteType;
import cn.xgame.a.player.fleet.info.FleetInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.net.netty.Netty.RW;

/**
 * 投票状态
 * @author deng		
 * @date 2015-10-30 下午1:14:04
 */
public class VoteStatus extends IStatus{
	
	// 起始时间
	private int starttime;
	
	// 持续时间
	private int conttime;
	
	// 同意的玩家UID
	private List<String> agrees  = Lists.newArrayList();
	
	// 投票类型 1.副本投票 2.踢人投票
	private IVoteType vtype = null;
	
	
	public VoteStatus() {
		super(StatusType.VOTE);
	}

	@Override
	public void init(Object[] objects) {
		int i = 0;
		starttime	= (Integer) objects[i++];
		conttime	= (Integer) objects[i++];
		List<?>	x	= (List<?>) objects[i++];
		for( Object o : x )
			agrees.add( (String) o );
		vtype		= (IVoteType) objects[i++];
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		// 不用保存数据库
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		// 不用保存数据库
	}

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		super.buildTransformStream(buffer);
		buffer.writeInt( getAlreadytime() );
		buffer.writeInt( conttime );
		buffer.writeByte( agrees.size() );
		for( String uid : agrees ){
			RW.writeString(buffer, uid);
		}
		vtype.buildTransformStream(buffer);
	}
	
	// 获取已经过的时间 - 只用到前端显示
	private int getAlreadytime() {
		return (int) (System.currentTimeMillis()/1000) - starttime;
	}
	
	@Override
	public boolean isComplete() {
		return (int) (System.currentTimeMillis()/1000) >= (starttime+conttime);
	}

	@Override
	public void update(FleetInfo fleet, Player player) {
		
		fleet.changeStatus( StatusType.HOVER );
	}

	public List<String> getAgrees() {
		return agrees;
	}
	public int getStarttime() {
		return starttime;
	}
	public int getConttime() {
		return conttime;
	}
	public IVoteType getVtype() {
		return vtype;
	}

}
