package cn.xgame.a.player;


import io.netty.buffer.ByteBuf;

import java.util.List;

import x.javaplus.collections.Lists;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.prop.IDepot;
import cn.xgame.a.prop.IProp;

/**
 * 一个舰船仓库基类
 * @author deng		
 * @date 2015-7-23 下午5:28:34
 */
public class IHold extends IDepot implements ITransformStream{

	// 货仓里面的唯一ID
	protected int propUID = 0;
	private int getResUID(){ return ++propUID; }
	
	// 货仓空间
	protected short room ;
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeShort( room );
		buffer.writeByte( props.size() );
		for( IProp o : props ){
			o.putBaseBuffer(buffer);
		}
	}
	
	/**
	 * 添加一个道具
	 * @param prop
	 */
	public List<IProp> appendProp( IProp prop ) {
		
		List<IProp> ret = Lists.newArrayList();
		// 获取一个可累加道具
		IProp temp 	= getCanCumsumProp( prop.getnId() );
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
		prop.setuId( getResUID() );
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

		int x = prop.deductCount(count);
		if( prop.isEmpty() )
			remove(prop);
		
		IProp ret = prop.clone();
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
		IProp temp = getCanCumsumProp( prop.getnId() );
		if( temp == null ){
			sum += prop.occupyRoom();
		}else{
			// 这里如果可以累加 那么看 是不是 会超出
			if( (temp.getCount() + prop.getCount()) > temp.item().manymax ){
				sum += prop.occupyRoom();
			}
		}
		return sum > room;
	}
	
	public short getRoom() {
		return room;
	}
	public void setRoom(short hroom) {
		this.room = hroom;
	}
	
	/**
	 * 货仓里面道具个数
	 * @return
	 */
	public short size() {
		return (short) props.size();
	}


	
}
