package cn.xgame.a.player.manor.info;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.manor.classes.IBuilding;

/**
 * 基地建筑 - 主操控
 * @author deng		
 * @date 2015-12-9 下午8:21:48
 */
public class BaseBuilding extends IBuilding{

	public BaseBuilding(int id) {
		super(id);
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeByte( getIndex() );
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		setIndex(buf.readByte());
	}
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( templet().id );
		buffer.writeByte( getIndex() );
	}
	
}
