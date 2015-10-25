package cn.xgame.a.player.tavern;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.tavern.info.TavernData;
import cn.xgame.a.player.u.Player;

/**
 * 酒馆
 * @author deng		
 * @date 2015-8-22 下午2:16:03
 */
public class TavernControl implements IArrayStream{

	// 酒馆列表 - 每个星球一个
	private List<TavernData> datas = Lists.newArrayList();
	
	public TavernControl( Player player ) {
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		datas.clear();
		ByteBuf buf = Unpooled.copiedBuffer(data);
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			TavernData o = new TavernData( buf.readInt() );
			o.wrapBuffer(buf);
			datas.add(o);
		}
	}

	@Override
	public byte[] toBytes() {
		if( datas.isEmpty() ) return null;
		ByteBuf buf = Unpooled.buffer(  );
		buf.writeByte( datas.size() );
		for( TavernData o : datas ){
			buf.writeInt( o.getSnid() );
			o.putBuffer(buf);
		}
		return buf.array();
	}

	/**
	 * 获取对应星球 酒馆数据
	 * @param snid
	 */
	private TavernData get( int snid ) {
		for( TavernData data : datas ){
			if( data.getSnid() == snid )
				return data;
		}
		return null;
	}
	
	/**
	 * 获取对应星球 酒馆数据 并刷新
	 * @param snid
	 * @return
	 */
	public TavernData getTavern( int snid ){
		TavernData data = get( snid );
		if( data == null ){
			data = new TavernData( snid );
			data.initEndtime();
			data.generateTaverns();
			datas.add( data );
		}else{
			data.updateTavern();
		}
		return data;
	}
	
}
