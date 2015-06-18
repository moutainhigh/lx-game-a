package cn.xgame.a.player;

import io.netty.buffer.ByteBuf;

/**
 * 允许在网络上传输的实体类
 * @author deng		
 * @date 2015-6-16 下午5:59:51
 */
public interface ITransformStream {

	/**
	 * 构建用于网络传输的ByteBuf
	 * @param	buffer			把数据放入此buffer
	 */
	void buildTransformStream( ByteBuf buffer );
}
