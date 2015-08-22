package cn.xgame.a.player.tavern;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import x.javaplus.collections.Lists;

import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.StarsPo;

/**
 * 酒馆
 * @author deng		
 * @date 2015-8-22 下午2:16:03
 */
public class TavernControl implements IArrayStream{

	@SuppressWarnings("unused")
	private final Player root;
	
	// 酒馆列表 - 每个星球一个
	private List<TavernData> datas = Lists.newArrayList();
	
	public TavernControl( Player player ) {
		this.root = player;
	}

	/** 初始化 */
	public void init() {
		
		for( StarsPo star : CsvGen.starspos ){
			
			TavernData o = new TavernData();
			
			o.setSnid( star.id );
			o.updateCaptain();
			
			datas.add( o );
		}
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ){
			init();
			return;
		}
		datas.clear();
		ByteBuf buf = Unpooled.copiedBuffer(data);
		byte size = buf.readByte();
		for( int i = 0; i < size; i++ ){
			TavernData o = new TavernData();
			o.wrapBuffer(buf);
			if( o.isUpdate() ){
				o.updateCaptain();
			}
			datas.add(o);
		}
	}

	@Override
	public byte[] toBytes() {
		if( datas.isEmpty() ) return null;
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeByte( datas.size() );
		for( TavernData o : datas )
			o.putBuffer(buf);
		return buf.array();
	}

	/**
	 * 获取对应星球 酒馆数据
	 * @param snid
	 */
	public TavernData get( int snid ) {
		for( TavernData data : datas ){
			if( data.getSnid() == snid ){
				return data;
			}
		}
		return null;
	}

}
