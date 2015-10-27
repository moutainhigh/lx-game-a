package cn.xgame.a.award;

import x.javaplus.util.Util.Random;
import io.netty.buffer.ByteBuf;

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
	
	public DropAward( String[] x ) {
		super( Integer.parseInt(x[0]), Integer.parseInt(x[1]) );
		this.rand = Integer.parseInt(x[1]);
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
