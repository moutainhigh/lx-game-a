package cn.xgame.a.player.dock.ship.o;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.prop.IDepot;
import cn.xgame.a.prop.IProp;

/**
 * 一个舰船仓库基类
 * @author deng		
 * @date 2015-7-23 下午5:28:34
 */
public class IHold extends IDepot implements ITransformStream,IArrayStream{

	// 货仓里面的唯一ID
	private static int propUID = 0;
	
	// 空间
	private short room;
	
	@Override
	public void fromBytes( byte[] data ) {
		if( data == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		room 		= buf.readShort();
		short size 	= buf.readByte();
		for( int i = 0; i < size; i++ ){
			IProp prop 		= IProp.create( buf );
			super.append( prop );
			
			// 得出最大的唯一ID
			if( prop.getUid() > propUID ) 
				propUID = prop.getUid();
		}
	}

	@Override
	public byte[] toBytes() {
		ByteBuf buf = Unpooled.buffer( 1024 );
		buildTransformStream( buf );
		return buf.array();
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeShort( room );
		buffer.writeByte( props.size() );
		for( IProp o : props ){
			o.putBuffer(buffer);
		}
	}
	
	public short getRoom() { return room; }
	public void setRoom( short room ) { this.room = room; }
	/* 货仓里面道具个数 */
	public short size() { return (short) props.size(); }
	private int getResUID(){ return ++propUID; }
	
	/**
	 * 添加一个道具
	 * @param prop
	 */
	public List<IProp> appendProp( IProp prop ) {
		
		List<IProp> ret = Lists.newArrayList();
		// 获取一个可累加道具
		IProp temp 	= getCanCumsumProp( prop.getNid() );
		if( temp == null ){
			ret.add( put( prop ) );
		}else{
			// 算出差值
			int surplus = temp.addCount( prop.getCount() );
			ret.add(temp);
			if( surplus > 0 ){
				prop.setCount(surplus);
				ret.add( put( prop ) );
			}
		}
		return ret;
	}
	
	/**
	 * 自己写一个 添加道具函数 方便修改唯一ID
	 * @param prop
	 * @return
	 */
	public IProp put( IProp prop ){
		prop.setUid( getResUID() );
		append(prop);
		return prop;
	}
	
	/**
	 * 扣除道具
	 * @param puid
	 * @param count
	 */
	public IProp deductProp( int puid, int count ) {
		
		IProp prop = getProp(puid);
		if( prop == null )
			return null;
		// 先拷贝一个出来
		IProp ret 	= prop.clone();
		// 执行扣除
		int x 		= prop.deductCount(count);
		if( prop.isEmpty() )
			remove(prop);
		// 这里如果有不够的话 那么这里也要减掉
		ret.setCount(count-x);
		return ret;
	}
	
	/**
	 * 放入该道具后 空间是否足够
	 * @param prop
	 * @return
	 */
	public boolean roomIsEnough( IProp prop ) {
		short sum = getAllOccupyRoom();
		//先判断是否可以累加 
		IProp temp = getCanCumsumProp( prop.getNid() );
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
