package cn.xgame.a.player.manor.info;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.manor.classes.BType;
import cn.xgame.config.o.BbuildingPo;

/**
 * 普通建筑
 * @author deng		
 * @date 2015-12-19 下午5:00:08
 */
public class NormalBuilding extends IBuilding{

	public NormalBuilding(BType type, BbuildingPo templet) {
		super(type, templet);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		super.putBuffer(buf);
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		super.wrapBuffer(buf);
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
