package cn.xgame.a.world.planet.data.building;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;

/**
 * 星球建筑 操作中心
 * @author deng		
 * @date 2015-6-25 下午5:01:19
 */
public class BuildingControl implements IArrayStream,ITransformStream{

	// 建筑列表
	private List<Buildings> buildings = Lists.newArrayList();

	
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
		ByteBuf buf = Unpooled.copiedBuffer(data);
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			int id = buf.readInt();
			Buildings o = new Buildings( id );
			o.wrapBuffer(buf);
			buildings.add(o);
		}
	}

	@Override
	public byte[] toBytes() {
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeByte( buildings.size() );
		for( Buildings o : buildings ){
			buf.writeInt( o.templet().id );
			o.putBuffer( buf );
		}
		return buf.array();
	}


	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( buildings.size() );
		for( Buildings o : buildings ){
			o.buildTransformStream( buffer );
		}
	}


}
