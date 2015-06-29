package cn.xgame.a.world.planet.data.resource;

import java.util.List;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.prop.IDepot;
import cn.xgame.a.prop.IProp;

/**
 * 星球资源 操作中心
 * @author deng		
 * @date 2015-6-25 下午4:59:59
 */
public class ResourceControl extends IDepot implements IArrayStream,ITransformStream{

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return ;
		
	}

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		List<IProp> ls = getAll();
		buffer.writeShort( ls.size() );
		for( IProp prop : ls ){
			prop.putBaseBuffer(buffer);
		}
	}


}
