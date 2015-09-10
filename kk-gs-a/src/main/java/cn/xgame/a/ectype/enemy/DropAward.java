package cn.xgame.a.ectype.enemy;

import x.javaplus.util.Util.Random;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.award.AwardInfo;

/**
 * 掉落
 * @author deng		
 * @date 2015-7-31 上午7:24:08
 */
public class DropAward extends AwardInfo {

	private int rand ;
	
	public DropAward(ByteBuf buf) {
		super(buf);
	}

	public DropAward(int id, int count, int rand) {
		super(id,count);
		this.rand = rand;
	}

	public int getRand() {
		return rand;
	}
	public void setRand(int rand) {
		this.rand = rand;
	}
	
	/**
	 * 是否掉落
	 * @return
	 */
	public boolean isDrop() {
		return Random.get( 0, 10000 ) <= rand;
	}
	
}
