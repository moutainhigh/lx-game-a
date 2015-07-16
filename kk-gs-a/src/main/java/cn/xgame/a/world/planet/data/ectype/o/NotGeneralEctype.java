package cn.xgame.a.world.planet.data.ectype.o;

import cn.xgame.a.world.planet.data.ectype.IEctype;

/**
 * 一个非常规副本
 * @author deng		
 * @date 2015-7-17 上午2:14:17
 */
public class NotGeneralEctype extends IEctype {

	private int rTime = -1;
	
	public NotGeneralEctype(int id) {
		super(id);
	}

	public int getrTime() {
		return rTime;
	}
	public void setrTime(int rTime) {
		this.rTime = rTime;
	}

}
