package cn.xgame.a.player.bag;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.ITransformStream;
import cn.xgame.a.player.prop.IProp;

/**
 * 一个小格子
 * @author deng		
 * @date 2015-6-17 下午6:51:55
 */
public class Bag extends IBag implements ITransformStream{
	
	// 对象
	private IProp prop;

	
	
	
	
	
	
	public Bag( IProp prop ) {
		this.prop = prop;
	}
	
	
	public IProp getProp() {
		return prop;
	}
	public void setProp(IProp prop) {
		this.prop = prop;
	}
	
	
	@Override
	public void buildTransformStream( ByteBuf response ) {
		prop.buildTransformStream( response );
	}
	
	/** 塞入基础数据 */
	public void putBaseBuffer( ByteBuf response ) {
		prop.putBaseBuffer( response );
	}
	
	public String toString(){
		return prop.toString();
	}



	
}
