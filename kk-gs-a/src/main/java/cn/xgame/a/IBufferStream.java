package cn.xgame.a;

import io.netty.buffer.ByteBuf;

public interface IBufferStream {

	/**
	 * 将数据 放入buf
	 * @param buf
	 */
	void putBuffer( ByteBuf buf );
	
	/**
	 * 获取buf数据
	 * @param buf
	 */
	void wrapBuffer( ByteBuf buf );
}
