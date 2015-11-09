package cn.xgame.a.player.swop;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.swop.info.SpecialSwop;
import cn.xgame.a.player.u.Player;

/**
 * 兑换管理中心
 * @author deng		
 * @date 2015-11-6 下午6:12:44
 */
public class SwopManager implements IArrayStream{
	
	// 特殊兑换记录
	private List<SpecialSwop> specials = Lists.newArrayList();
	
	
	public SwopManager(Player player) {
	}
	
	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		short size = buf.readShort();
		for (int i = 0; i < size; i++) {
			SpecialSwop o = new SpecialSwop();
			o.wrapBuffer(buf);
			specials.add(o);
		}
		buf.release();
	}

	@Override
	public byte[] toBytes() {
		if( specials.isEmpty() )
			return null;
		ByteBuf buf = Unpooled.buffer();
		buf.writeShort( specials.size() );
		for( SpecialSwop swop : specials )
			swop.putBuffer(buf);
		return buf.array();
	}
	
	public List<SpecialSwop> getSpecials() {
		return specials;
	}
	
	public void clear(){
		specials.clear();
	}

	public List<SpecialSwop> getSSwops( int sid ) {
		List<SpecialSwop> ret = Lists.newArrayList();
		for( SpecialSwop swop : specials ){
			if( swop.getSind() == sid )
				ret.add(swop);
		}
		return ret;
	}
	
	/**
	 * 根据星球ID 和道具ID 获取对应兑换道具
	 * @param sid
	 * @param id
	 * @return
	 */
	public SpecialSwop getSwopInfo( int sid, int id ){
		for( SpecialSwop swop : specials ){
			if( swop.getSind() == sid && swop.getId() == id )
				return swop;
		}
		return null;
	}

	
}
