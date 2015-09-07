package cn.xgame.a.world.planet.data.resource;

import java.util.List;

import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.prop.IDepot;
import cn.xgame.a.prop.IProp;
import cn.xgame.utils.Logs;

/**
 * 星球资源 操作中心
 * @author deng		
 * @date 2015-6-25 下午4:59:59
 */
public class ResourceControl extends IDepot implements IArrayStream,ITransformStream{

	public final int SNID;
	
	// 资源唯一ID
	private int resUID = 0;
	
	public ResourceControl(int id) {
		SNID = id;
	}

	private int getResUID(){ return ++resUID; }
	
	@Override
	public void fromBytes( byte[] data ) {
		if( data == null ) return ;
		
		props.clear();
		resUID = 0;
		
		ByteBuf buf = Unpooled.copiedBuffer(data);
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			IProp prop = IProp.create( buf );
			super.append( prop );
			
			// 得出最大的唯一ID
			if( prop.getUid() > resUID ) resUID = prop.getUid();
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
			prop.putBuffer(buf);
		}
		return buf.array();
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		List<IProp> ls = getAll();
		buffer.writeByte( ls.size() );
		for( IProp prop : ls ){
			buffer.writeInt( prop.getUid() );
			buffer.writeInt( prop.getNid() );
			buffer.writeInt( prop.getCount() );
		}
	}

	/**
	 * 添加一个资源
	 * @param prop
	 */
	public List<IProp> appendProp( IProp param ){
		List<IProp> ret = Lists.newArrayList();
		IProp prop = getCanCumsumProp( param );
		if( prop == null ){
			zappend( param );
			ret.add( param );
		}else{
			int surplus = prop.addCount( param.getCount() );
			ret.add( prop );
			// 这里说明还有剩余的
			if( surplus > 0 ){
				param.setCount( surplus );
				zappend( param );
				ret.add( param );
			}
		}
		
		return ret;
	}

	/** 这里自己封一个 为了给物品一个唯一ID */
	private void zappend( IProp prop ){
		prop.setUid( getResUID() );
		super.append( prop );
	}
	
	/**
	 * 扣除资源
	 * @param needres
	 * @return
	 */
	public boolean deductProp( String needres ) {
		if( needres == null || needres.isEmpty() )
			return true;
		
		// 临时记录 做出操作的道具
		List<IProp> temp = Lists.newArrayList(); 
		
		String[] content = needres.split("\\|");
		for( String x : content ){
			if( x.isEmpty() ) continue;
			String[] o 	= x.split(";");
			int nid 	= Integer.parseInt( o[0] );
			int count	= Integer.parseInt( o[1] );
			// 获取 对应ID的 所有道具列表
			List<IProp> props = getPropInClone( nid );
			if( props.isEmpty() ) return false;
			// 下面开始扣除 拷贝的数据
			for( IProp prop : props ){
				count	= prop.deductCount( count );
				temp.add( prop );
				if( count == 0 ) break;
			}
			// 判断如果数量还有 那么直接返回 false
			if( count > 0 ) return false;
		}
		// 这里代表 道具全部扣完  把临时记录的拿来 真真扣除
		for( IProp prop : temp ){
			if( prop.isEmpty() )
				remove( prop );
			else
				getProp( prop ).setCount( prop.getCount() );
			Logs.debug( "星球" + SNID + " 扣除资源 " + prop );
		}
		return true;
	}

	// 获取一份拷贝的道具
	private List<IProp> getPropInClone( int nid ) {
		List<IProp> ret = Lists.newArrayList();
		List<IProp> ls = getAll();
		for( IProp prop : ls ){
			if( prop.getNid() == nid ){
				ret.add( prop.clone() );
			}
		}
		return ret;
	}


	
	
	


}
