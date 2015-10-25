package cn.xgame.a.prop.other;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.Quality;
import cn.xgame.config.o.ItemPo;

/**
 * 其他道具
 * @author deng		
 * @date 2015-10-25 下午8:09:18
 */
public class OtherAttr extends IProp{
	
	
	public OtherAttr( ItemPo item, int uid, int nid, int count, Quality quality ) {
		super( item, uid, nid, count, quality );
	}
	
	private OtherAttr( OtherAttr clone ){
		super( clone );
	}
	
	@Override
	public OtherAttr clone() { return new OtherAttr(this); }

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
