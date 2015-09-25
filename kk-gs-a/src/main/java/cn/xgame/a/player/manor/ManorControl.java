package cn.xgame.a.player.manor;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.manor.o.Bbuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.o.ReclaimcapacityPo;

/**
 * 玩家领地 操作中心
 * @author deng		
 * @date 2015-6-23 下午12:35:43
 */
public class ManorControl implements IArrayStream,ITransformStream{

	// 领土
	private ReclaimcapacityPo territory;
	
	// 建筑列表
	private List<Bbuilding> builds = Lists.newArrayList();
	
	
	public ManorControl(Player player) {
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		
	}

	@Override
	public byte[] toBytes() {
		if( territory == null ) return null;
		ByteBuf buff = Unpooled.buffer();
		
		return buff.array();
	}

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		// TODO Auto-generated method stub
		
	}

	public ReclaimcapacityPo getTerritory() {
		return territory;
	}
	public void setTerritory(ReclaimcapacityPo territory) {
		this.territory = territory;
	}
	public List<Bbuilding> getBuilds() {
		return builds;
	}

	/**
	 * 领地ID
	 * @return
	 */
	public int getNid() {
		return territory == null ? 0 : territory.id;
	}

}
