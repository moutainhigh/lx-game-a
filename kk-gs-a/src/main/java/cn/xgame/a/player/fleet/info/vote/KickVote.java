package cn.xgame.a.player.fleet.info.vote;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.fleet.classes.IVoteType;

/**
 * 踢人投票
 * @author deng		
 * @date 2015-10-30 下午3:20:40
 */
public class KickVote extends IVoteType{

	public KickVote() {
		super((byte) 2);
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		super.buildTransformStream(buffer);
	}
	
}
