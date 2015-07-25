package cn.xgame.a.player.ship.o.v;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.IHold;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;

/**
 * 所有武器
 * @author deng		
 * @date 2015-7-23 下午4:43:38
 */
public class WeaponControl extends IHold implements IArrayStream{


	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		props.clear();
		propUID 	= 0;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		room 		= buf.readShort();
		short size 	= buf.readShort();
		for( int i = 0; i < size; i++ ){
			PropType type 	= PropType.fromNumber( buf.readByte() );
			int uid			= buf.readInt();
			int nid 		= buf.readInt();
			int count 		= buf.readInt();
			IProp prop 		= type.create( uid, nid, count );
			prop.wrapAttach( buf );
			super.append( prop );
			
			// 得出最大的唯一ID
			if( uid > propUID ) propUID	= uid;
		}
	}

	@Override
	public byte[] toBytes() {
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeShort( room );
		buf.writeShort( props.size() );
		for( IProp o : props ){
			buf.writeByte( o.type().toNumber() );
			o.putBaseBuffer(buf);
			o.putAttachBuffer(buf);
		}
		return buf.array();
	}
	
}
