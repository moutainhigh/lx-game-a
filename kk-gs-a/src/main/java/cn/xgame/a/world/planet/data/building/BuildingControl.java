package cn.xgame.a.world.planet.data.building;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;

/**
 * 星球建筑 操作中心
 * @author deng		
 * @date 2015-6-25 下午5:01:19
 */
public class BuildingControl implements IArrayStream{

	// 已建筑的建筑列表
	private List<Buildings> buildings = Lists.newArrayList();
	
	// 投票建筑列表
	private List<UnBuildings> voBuildings = Lists.newArrayList();
	
	// 建筑中列表
	private List<UnBuildings> unBuildings = Lists.newArrayList();
	
	
	public void fromTemplet( String content ) {
		if( content.isEmpty() ) return;
		String[] ls = content.split("\\|");
		for( int i = 0; i < ls.length; i++ ){
			if( ls[i].isEmpty() ) continue;
			String[] x 	= ls[i].split( ";" );
			Buildings o = new Buildings( Integer.parseInt( x[0] ) );
			o.setIndex( Byte.parseByte( x[1] ) );
			buildings.add(o);
		}
	}
	
	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		
		buildings.clear();
		voBuildings.clear();
		unBuildings.clear();
		
		ByteBuf buf = Unpooled.copiedBuffer(data);
		// 已建筑
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			int id = buf.readInt();
			Buildings o = new Buildings( id );
			o.wrapBuffer(buf);
			buildings.add(o);
		}
		// 投票中
		size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			int id = buf.readInt();
			UnBuildings o = new UnBuildings( id );
			o.wrapBuffer(buf);
			o.getVote().wrapBuffer(buf);
			voBuildings.add(o);
		}
		// 建筑中
		size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			int id = buf.readInt();
			UnBuildings o = new UnBuildings( id );
			o.wrapBuffer(buf);
			o.setrTime( buf.readInt() );
			unBuildings.add(o);
		}
	}

	@Override
	public byte[] toBytes() {
		ByteBuf buf = Unpooled.buffer( 1024 );
		// 已建筑
		buf.writeByte( buildings.size() );
		for( Buildings o : buildings ){
			buf.writeInt( o.templet().id );
			o.putBuffer( buf );
		}
		// 投票中
		buf.writeByte( voBuildings.size() );
		for( UnBuildings o : voBuildings ){
			buf.writeInt( o.templet().id );
			o.putBuffer( buf );
			o.getVote().putBuffer( buf );
		}
		// 建筑中
		buf.writeByte( unBuildings.size() );
		for( UnBuildings o : unBuildings ){
			buf.writeInt( o.templet().id );
			o.putBuffer( buf );
			buf.writeInt( o.getrTime() );
		}
		return buf.array();
	}


	/** 塞入 所有建筑 */
	public void putBuilding( ByteBuf buffer ) {
		buffer.writeByte( buildings.size() );
		for( Buildings o : buildings ){
			o.buildTransformStream( buffer );
		}
	}
	/** 塞入 投票建筑 */
	public void putVoBuilding( ByteBuf buffer ) {
		buffer.writeByte( voBuildings.size() );
		for( UnBuildings o : voBuildings ){
			o.buildTransformStream( buffer );
			o.getVote().buildTransformStream(buffer);
		}
	}
	/** 塞入 建筑中 */
	public void putUnBuilding( ByteBuf buffer ) {
		buffer.writeByte( unBuildings.size() );
		for( UnBuildings o : unBuildings ){
			o.buildTransformStream( buffer );
			buffer.writeInt( o.getrTime() );
		}
	}
	
}
