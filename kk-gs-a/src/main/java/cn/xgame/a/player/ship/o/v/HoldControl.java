package cn.xgame.a.player.ship.o.v;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.IHold;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;

/**
 * 一个货仓
 * @author deng		
 * @date 2015-7-23 下午2:10:16
 */
public class HoldControl extends IHold implements IArrayStream{
	
	// 空间
	private short room;
	
	public short getRoom() {
		return room;
	}
	public void setRoom(short room) {
		this.room = room;
	}
	
	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		props.clear();
		propUID 	= 0;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		setRoom(buf.readShort());
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
		buf.writeShort( getRoom() );
		buf.writeShort( props.size() );
		for( IProp o : props ){
			buf.writeByte( o.type().toNumber() );
			o.putBaseBuffer(buf);
			o.putAttachBuffer(buf);
		}
		return buf.array();
	}

	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeShort( room );
		super.buildTransformStream(buffer);
	}
	
	/**
	 * 放入该道具后 空间是否足够
	 * @param prop
	 * @return
	 */
	public boolean roomIsEnough( IProp prop ) {
		short sum = getAllOccupyRoom();
		//先判断是否可以累加 
		IProp temp = getCanCumsumProp( prop.getnId() );
		if( temp == null ){
			sum += prop.occupyRoom();
		}else{
			// 这里如果可以累加 那么看 是不是 会超出
			if( (temp.getCount() + prop.getCount()) > temp.item().manymax ){
				sum += prop.occupyRoom();
			}
		}
		return sum <= room;
	}


}
