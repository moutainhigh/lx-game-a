package cn.xgame.a.world.planet.data.tavern;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;

/**
 * 一个舰长数据
 * @author deng		
 * @date 2015-7-16 上午10:09:26
 */
public class TCaptain implements ITransformStream{

	// 舰长表格ID
	private int nid;
	
	
	public TCaptain(int id) {
		nid = id;
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( nid );
	}

	public String toString(){
		return "nid=" + nid;
	}

	public int getNid() {
		return nid;
	}
	public void setNid(int nid) {
		this.nid = nid;
	}
}
