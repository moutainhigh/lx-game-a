package cn.xgame.a.player.ectype;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.ectype.o.AccEctype;
import cn.xgame.a.player.ectype.o.AccType;
import cn.xgame.a.player.u.Player;
import cn.xgame.utils.Logs;

/**
 * 偶发副本信息
 * @author deng		
 * @date 2015-7-17 上午3:23:52
 */
public class AccEctypeControl implements IArrayStream,ITransformStream{

	private Player root;
	
	private List<AccEctype> ectypes = Lists.newArrayList();
	
	// 需要删除的列表
	private List<Integer> removes = Lists.newArrayList();
	
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
			if( !acc.isClose() )
				ectypes.add( acc );
		}
	}

	@Override
	public byte[] toBytes() {
		if( ectypes.isEmpty() ) return null;
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeByte( ectypes.size() );
		for( AccEctype acc : ectypes )
			acc.putBuffer(buf);
		return buf.array();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( ectypes.size() );
		for( AccEctype acc : ectypes ){
			acc.buildTransformStream(buffer);
		}
		Logs.debug( root, "申请副本 " + ectypes );
	}
	
	
	public AccEctype getEctyper( int nid ) {
		for( AccEctype o : ectypes ){
			if( o.getNid() == nid )
				return o;
		}
		return null;
	}
	
	public void remove( int nid ){
		Iterator<AccEctype> iter = ectypes.iterator();
		while( iter.hasNext() ){
			AccEctype o = iter.next();
			if( o.getNid() == nid ){
				iter.remove();
				return;
			}
		}
	}
	
	public void append( List<AccEctype> v ) {
		ectypes.addAll(v);
	}

	public void clear() {
		ectypes.clear();
	}

	/** 玩家登录 记录登录计时副本 */
	public void startRLoginTime() {
		for( AccEctype o : ectypes ){
			if( o.type() != AccType.LOGINTIME )
				continue;
			if( o.getEndTime() != -1 )
				continue;
			o.setEndTime();
		}
	}

	/**
	 * 线程 这里检测副本是否关闭 然后删除掉
	 */
	public void run(){
		
		for( AccEctype o : ectypes ){
			if( o.isClose() ){
				removes.add( o.getNid() );
				Logs.debug( root, "副本" + o.getNid() + " 已加入删除列表" );
			}
		}
		
		while( !removes.isEmpty() )
			remove( removes.remove(0) );
	}
	
}
