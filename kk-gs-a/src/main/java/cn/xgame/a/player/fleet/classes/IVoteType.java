package cn.xgame.a.player.fleet.classes;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;

/**
 * 投票附加属性
 * @author deng		
 * @date 2015-10-30 下午1:38:53
 */
public class IVoteType implements ITransformStream{
	
	private byte type;
	
	public IVoteType(byte type) {
		this.type = type;
	}

	/**
	 * 类.型 1.副本投票 2.踢人投票
	 * @return
	 */
	public byte type(){ return type; }

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte(type);
	}
	
}
