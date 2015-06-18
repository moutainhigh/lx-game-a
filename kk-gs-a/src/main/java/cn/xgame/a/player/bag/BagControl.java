package cn.xgame.a.player.bag;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import cn.xgame.a.player.IArrayStream;
import cn.xgame.a.player.ITransformStream;

import x.javaplus.collections.Lists;

/**
 * 背包操作中心
 * @author deng		
 * @date 2015-6-17 下午6:56:27
 */
public class BagControl implements IArrayStream, ITransformStream{

	// 背包列表
	private List<Bag> bags = Lists.newArrayList();

	
	@Override
	public void fromBytes( byte[] data ) {
		bags.clear();
		ByteBuf buffer = Unpooled.copiedBuffer(data);
		short len = buffer.readShort();
		for( int i = 0; i < len; i++ ){
			Bag bag = new Bag();
			bag.putBuffer( buffer );
			bags.add(bag);
		}
	}
	
	@Override
	public byte[] toBytes() {
		ByteBuf buffer = Unpooled.buffer(2048);
		buffer.writeShort( bags.size() );
		for( Bag bag : bags ){
			bag.toBuffer( buffer );
		}
		return buffer.array();
	}
	
	
	public List<Bag> getBags() {
		return bags;
	}
	public void setBags(List<Bag> bags) {
		this.bags = bags;
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeShort( bags.size() );
		for( Bag bag : bags ){
			bag.buildTransformStream( buffer );
		}
	}
	
	
	

	
	
	
}
