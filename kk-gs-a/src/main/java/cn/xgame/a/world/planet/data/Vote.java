package cn.xgame.a.world.planet.data;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;

/**
 * 投票器
 * @author deng		
 * @date 2015-6-30 下午5:04:43
 */
public class Vote implements ITransformStream , IBufferStream{

	// 同意数
	private int agrees;
	
	// 不同意数
	private int disagrees;
	
	// 时间限制 单位-秒
	private int timeRestrict;
	
	public Vote( int time ) {
		timeRestrict = time;
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putBuffer(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wrapBuffer(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	public int getTimeRestrict() {
		return timeRestrict;
	}
	public void setTimeRestrict(int timeRestrict) {
		this.timeRestrict = timeRestrict;
	}
	public int getDisagrees() {
		return disagrees;
	}
	public void setDisagrees(int disagrees) {
		this.disagrees = disagrees;
	}
	public int getAgrees() {
		return agrees;
	}
	public void setAgrees(int agrees) {
		this.agrees = agrees;
	}

	public void setIsAgrees( byte isAgree ) {
		if( isAgree == 1 )
			++agrees;
		else
			++disagrees;
	}

}
