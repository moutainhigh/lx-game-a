package cn.xgame.a.world.planet.data.specialty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;

/**
 * 特产 操作中心
 * @author deng		
 * @date 2015-6-26 上午11:37:14
 */
public class SpecialtyControl implements IArrayStream,ITransformStream{

	// 特产列表
	private List<Specialty> specialtys = Lists.newArrayList();
	
	
	/**
	 * 通过配置表 初始化
	 * @param content
	 */
	public void fromTemplet( String content ) {
		if( content.isEmpty() ) return;
		String[] ls = content.split("\\|");
		for( int i = 0; i < ls.length; i++ ){
			if( ls[i].isEmpty() ) continue;
			String[] x 	= ls[i].split( ";" );
			Specialty o = new Specialty( Integer.parseInt( x[0] ) );
			o.setYieldTime( Integer.parseInt( x[1] ) );
			o.setYieldNum( Integer.parseInt( x[2] ) );
			specialtys.add(o);
		}
	}

	@Override
	public void fromBytes( byte[] data ) {
		ByteBuf buf = Unpooled.copiedBuffer(data);
		short size = buf.readShort();
		for( int i = 0; i < size; i++ ){
			int id = buf.readInt();
			Specialty o = new Specialty( id );
			o.wrapBuffer(buf);
			specialtys.add(o);
		}
	}

	@Override
	public byte[] toBytes() {
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeShort( specialtys.size() );
		for( Specialty o : specialtys ){
			buf.writeInt( o.templet().id );
			o.putBuffer( buf );
		}
		return buf.array();
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeShort( specialtys.size() );
		for( Specialty o : specialtys ){
			o.buildTransformStream( buffer );
		}
	}

	

}
