package cn.xgame.a.player.ship.o;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.IUObject;
import cn.xgame.gen.dto.MysqlGen.ShipsDto;

/**
 * 一个舰船 的信息
 * @author deng		
 * @date 2015-7-9 下午12:22:54
 */
public class ShipInfo extends IUObject implements ITransformStream{

	// 舰长唯一ID
	private int captainUID;
	// 停靠在的星球
	private int starId;
	
	// 组队信息
	// TODO
	
	public ShipInfo( int uid, int nid ) {
		setuId(uid);
		setnId(nid);
	}

	public ShipInfo( ShipsDto dto ) {
		setuId(dto.getUid());
		setnId(dto.getNid());
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( getuId() );
		buffer.writeInt( getnId() );
	}

	
	/**
	 * 获取到某个星球的航行时间 单位秒
	 * @param starId
	 * @return
	 */
	public int getSailingTime( int starId ) {
		return 100;
	}
	
	
	public int getCaptainUID() {
		return captainUID;
	}
	public void setCaptainUID(int captainUID) {
		this.captainUID = captainUID;
	}
	public int getStarId() {
		return starId;
	}
	public void setStarId(int starId) {
		this.starId = starId;
	}
	
}
