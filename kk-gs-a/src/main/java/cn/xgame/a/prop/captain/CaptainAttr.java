package cn.xgame.a.prop.captain;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.CaptainPo;
import cn.xgame.config.o.ItemPo;

/**
 * 舰长属性
 * @author deng		
 * @date 2015-6-17 下午7:19:24
 */
public class CaptainAttr extends IProp {

	private final CaptainPo templet;
	

	public CaptainAttr( ItemPo item, int uid, int nid, int count ) {
		super( item, uid, nid, count );
		templet = CsvGen.getCaptainPo(nid);
	}

	private CaptainAttr( CaptainAttr clone ) {
		super( clone );
		templet = clone.templet;
	}
	
	@Override
	public CaptainAttr clone() { return new CaptainAttr( this ); }
	
	public CaptainPo templet(){ return templet; }
	
	@Override
	public byte[] toAttachBytes() {
		return null;
	}

	@Override
	public void wrapAttachBytes( byte[] bytes ) {
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
	}
	





}
