package cn.xgame.a.world.planet.data.resource;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.prop.IDepot;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;

/**
 * 星球资源 操作中心
 * @author deng		
 * @date 2015-6-25 下午4:59:59
 */
public class ResourceControl extends IDepot implements IArrayStream,ITransformStream{

	@Override
	public void fromBytes( byte[] data ) {
		if( data == null ) return ;
		props.clear();
		ByteBuf buf = Unpooled.copiedBuffer(data);
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			PropType type 	= PropType.fromNumber( buf.readByte() );
			int nid 		= buf.readInt();
			int count 		= buf.readInt();
			IProp prop 		= type.create( 0, nid, count );
			super.append( prop );
		}
	}

	@Override
	public byte[] toBytes() {
		List<IProp> ls = getAll();
		if( ls.isEmpty() ) 
			return null;
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeByte( ls.size() );
		for( IProp prop : ls ){
			buf.writeByte( prop.type().toNumber() );
			buf.writeInt( prop.getnId() );
			buf.writeInt( prop.getCount() );
		}
		return buf.array();
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		List<IProp> ls = getAll();
		buffer.writeByte( ls.size() );
		for( IProp prop : ls ){
			buffer.writeInt( prop.getnId() );
			buffer.writeInt( prop.getCount() );
		}
	}

	/**
	 * 添加一个资源
	 * @param prop
	 */
	public void appendProp( IProp param ){
		
		IProp prop = getCanAccProp( param );
		if( prop == null ){
			append( param );
		}else{
			int surplus = prop.addCount( param.getCount() );
			if( surplus > 0 ){
				param.setCount(surplus);
				append( param );
			}
		}
	}

}
