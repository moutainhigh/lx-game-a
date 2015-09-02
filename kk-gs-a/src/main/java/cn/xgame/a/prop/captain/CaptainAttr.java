package cn.xgame.a.prop.captain;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
	
	// 舰长品质
	private byte quality;

	public CaptainAttr( ItemPo item, int uid, int nid, int count ) {
		super( item, uid, nid, count );
		templet = CsvGen.getCaptainPo(nid);
	}

	private CaptainAttr( CaptainAttr clone ) {
		super( clone );
		templet = clone.templet;
		quality	= clone.quality;
	}
	
	@Override
	public CaptainAttr clone() { return new CaptainAttr( this ); }
	
	public CaptainPo templet(){ return templet; }
	
	@Override
	public byte[] toAttachBytes() {
		ByteBuf buf = Unpooled.buffer( 1 );
		buf.writeByte(quality);
		return buf.array();
	}

	@Override
	public void wrapAttachBytes( byte[] bytes ) {
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		quality = buf.readByte();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		
	}
	
	public byte getQuality() {
		return quality;
	}
	public void setQuality(byte quality) {
		this.quality = quality;
	}





}
