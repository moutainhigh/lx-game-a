package cn.xgame.a.player.ectype;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.ectype.o.AccEctype;
import cn.xgame.a.player.u.Player;

/**
 * 偶发副本信息
 * @author deng		
 * @date 2015-7-17 上午3:23:52
 */
public class AccEctypeControl implements IArrayStream,ITransformStream{

	private Player root;
	
	private List<AccEctype> ectypes = Lists.newArrayList();
	
	public AccEctypeControl(Player player) {
		root = player;
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		ectypes.clear();
		ByteBuf buf = Unpooled.copiedBuffer(data);
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			AccEctype acc = new AccEctype( buf );
			ectypes.add( acc );
		}
	}

	@Override
	public byte[] toBytes() {
		if( ectypes.isEmpty() ) return null;
		ByteBuf buf = Unpooled.buffer( 1024 );
		buildTransformStream( buf );
		return buf.array();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( ectypes.size() );
		for( AccEctype acc : ectypes ){
			acc.buildTransformStream(buffer);
		}
	}
	
	
	public AccEctype getEctyper( int nid ) {
		for( AccEctype o : ectypes ){
			if( o.getNid() == nid )
				return o;
		}
		return null;
	}
	
	public void append( List<AccEctype> v ) {
		ectypes.addAll(v);
	}

	public void clear() {
		ectypes.clear();
	}






	
}
