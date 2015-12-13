package cn.xgame.a.player.manor.info;

import cn.xgame.a.player.manor.classes.BType;
import cn.xgame.config.o.BbuildingPo;
import io.netty.buffer.ByteBuf;

/**
 * 基地建筑 - 主操控
 * @author deng		
 * @date 2015-12-9 下午8:21:48
 */
public class BaseBuilding extends IBuilding{

	public BaseBuilding(BType type,BbuildingPo templet) {
		super(type,templet);
	}
	
	@Override
	public void init() {
		
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
