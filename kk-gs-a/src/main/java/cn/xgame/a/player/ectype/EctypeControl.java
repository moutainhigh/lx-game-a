package cn.xgame.a.player.ectype;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.ectype.o.AccEctype;
import cn.xgame.a.player.ectype.o.PreEctype;
import cn.xgame.a.player.u.Player;
import cn.xgame.utils.Logs;

/**
 * 副本信息
 * @author deng		
 * @date 2015-7-17 上午3:23:52
 */
public class EctypeControl implements IArrayStream,ITransformStream{

	private Player root;
	
	// 偶发副本
	private List<AccEctype> accEctypes = Lists.newArrayList();

	// 本星球常驻副本（这里包括瞭望的）
	private List<PreEctype> preEctypes = Lists.newArrayList();
	
	// 瞭望范围外的副本 （就是根据某舰船停靠获得）
	private List<PreEctype> ourEctypes = Lists.newArrayList();
	
	// 需要删除的列表
	private List<Integer> removes = Lists.newArrayList();
	
	public EctypeControl(Player player) {
		root = player;
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		clear();
		ByteBuf buf = Unpooled.copiedBuffer(data);
		// 常驻副本
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			preEctypes.add( new PreEctype(buf) );
		}
		// 额外副本
		size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			ourEctypes.add( new PreEctype(buf) );
		}
		// 偶发副本
		size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			AccEctype acc = new AccEctype( buf );
			if( !acc.isClose() )
				accEctypes.add( acc );
		}
		
	}

	@Override
	public byte[] toBytes() {
		if( accEctypes.isEmpty() ) return null;
		ByteBuf buf = Unpooled.buffer( 1024 );
		// 常驻副本
		buf.writeByte( preEctypes.size() );
		for( PreEctype pre : preEctypes ){
			pre.putBuffer(buf);
		}
		// 额外副本
		buf.writeByte( ourEctypes.size() );
		for( PreEctype pre : ourEctypes ){
			pre.putBuffer(buf);
		}
		// 偶发副本
		buf.writeByte( accEctypes.size() );
		for( AccEctype acc : accEctypes )
			acc.putBuffer(buf);
		return buf.array();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		// 偶发副本个数 
		buffer.writeByte( accEctypes.size() );
		for( AccEctype acc : accEctypes ){
			acc.buildTransformStream(buffer);
		}
		// 常驻-本星球副本个数（包括瞭望的）
		buffer.writeByte( preEctypes.size() );
		for( PreEctype pre : preEctypes ){
			pre.buildTransformStream(buffer);
		}
		// 常驻-不在瞭望范围内的副本个数
		buffer.writeByte( ourEctypes.size() );
		for( PreEctype pre : ourEctypes ){
			pre.buildTransformStream(buffer);
		}
		Logs.debug( root, "偶发副本 " + accEctypes );
		Logs.debug( root, "常驻副本 " + preEctypes );
		Logs.debug( root, "额外副本 " + ourEctypes );
	}
	
	
	public AccEctype getEctyper( int nid ) {
		for( AccEctype o : accEctypes ){
			if( o.getNid() == nid )
				return o;
		}
		return null;
	}
	
	public void remove( int nid ){
		Iterator<AccEctype> iter = accEctypes.iterator();
		while( iter.hasNext() ){
			AccEctype o = iter.next();
			if( o.getNid() == nid ){
				iter.remove();
				return;
			}
		}
	}
	
	public void appendPre( PreEctype ectype ) {
		preEctypes.add(ectype);
	}
	public void appendAcc( List<AccEctype> v ) {
		accEctypes.addAll(v);
	}
	
	public void clear() {
		accEctypes.clear();
		preEctypes.clear();
		ourEctypes.clear();
	}

	/** 玩家登录 记录登录计时副本 */
	public void startRLoginTime() {
		for( AccEctype o : accEctypes ){
			if( o.type() != EctypeType.LOGINTIME )
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
		
		for( AccEctype o : accEctypes ){
			if( o.isClose() ){
				removes.add( o.getNid() );
				Logs.debug( root, "副本" + o.getNid() + " 已加入删除列表" );
			}
		}
		
		while( !removes.isEmpty() )
			remove( removes.remove(0) );
	}


	
}
