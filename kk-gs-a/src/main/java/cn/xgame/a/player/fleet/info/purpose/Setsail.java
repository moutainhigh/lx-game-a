package cn.xgame.a.player.fleet.info.purpose;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.classes.IPurpose;

/**
 *  出航
 * @author deng		
 * @date 2015-9-14 下午12:34:01
 */
public class Setsail extends IPurpose{

	// 航线
	private List<Integer> airline = Lists.newArrayList();
	
	
	public Setsail( List<Integer> args ) {
		super((byte) 2);
		airline.addAll(args);
	}

	public Setsail() {
		super((byte) 2);
	}

	@Override
	public void putBuffer( ByteBuf buf ) {
		buf.writeByte( airline.size() );
		for( int o : airline ){
			buf.writeInt( o );
		}
	}
	
	@Override
	public void wrapBuffer( ByteBuf buf ) {
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			airline.add( buf.readInt() );
		}
	}

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeByte( type() );
//		putBuffer( buffer );
	}

	public List<Integer> getAirline() {
		return airline;
	}
	
	/**
	 * 重置 航线
	 * @param args
	 */
	public void resetAirline( List<Integer> args ){
		airline.clear();
		airline.addAll(args);
	}
	
}
