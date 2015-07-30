package cn.xgame.a.player.ship.o.v;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.combat.o.AtkAndDef;
import cn.xgame.a.player.IHold;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Weapon;

/**
 * 所有装备 武器 and 辅助
 * @author deng		
 * @date 2015-7-23 下午4:43:38
 */
public class EquipControl extends IHold implements IArrayStream{
	
	// 武器空间
	private short wroom;
	// 辅助空间
	private short eroom;


	public short getWroom() {
		return wroom;
	}
	public void setWroom(short wroom) {
		this.wroom = wroom;
	}
	public short getEroom() {
		return eroom;
	}
	public void setEroom(short eroom) {
		this.eroom = eroom;
	}

	
	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		props.clear();
		propUID 	= 0;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		wroom 		= buf.readShort();
		eroom		= buf.readShort();
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
		buf.writeShort( wroom );
		buf.writeShort( eroom );
		buf.writeShort( props.size() );
		for( IProp o : props ){
			buf.writeByte( o.type().toNumber() );
			o.putBaseBuffer(buf);
			o.putAttachBuffer(buf);
		}
		return buf.array();
	}
	
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeShort( wroom );
		super.buildTransformStream(buffer);
		buffer.writeShort( eroom );
	}
	
	@Override
	public boolean roomIsEnough( IProp prop ) {
		short sum = getAllOccupyRoomInType( prop.item().itemtype );
		short room = prop.item().itemtype == 1 ? wroom : eroom;
		return sum + prop.occupyRoom() <= room;
	}
	
	
	/**
	 * 包装战斗属性
	 * @param attacks
	 * @param defends
	 */
	public void warpFightProperty(List<AtkAndDef> attacks, List<AtkAndDef> defends) {
		List<IProp> all = getAll();
		for( IProp prop : all ){
			Weapon weapon = CsvGen.getWeapon( prop.getnId() );
			AtkAndDef o = new AtkAndDef(  );
//			String
//			o.type = weapon.type;
//			o.type = 1;
//			
//			if( weapon.latk )
//			o.value = 
////				
			
			
		}
	}




	
}
