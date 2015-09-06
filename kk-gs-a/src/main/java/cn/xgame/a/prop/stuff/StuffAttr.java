package cn.xgame.a.prop.stuff;


import io.netty.buffer.ByteBuf;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.Quality;
import cn.xgame.config.o.ItemPo;

/**
 * 材料属性
 * @author deng		
 * @date 2015-6-17 下午7:46:35
 */
public class StuffAttr extends IProp{
	
	
	public StuffAttr( ItemPo item, int uid, int nid, int count, Quality quality ) {
		super( item, uid, nid, count, quality );
	}
	
	private StuffAttr( StuffAttr clone ){
		super( clone );
	}
	
	@Override
	public StuffAttr clone() { return new StuffAttr(this); }

	@Override
	public byte[] toAttachBytes() {
		return null;
	}

	@Override
	public void wrapAttachBytes( byte[] bytes ) {
		
	}

	@Override
	public void randomAttachAttr() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
	}
	
}
