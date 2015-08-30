package cn.xgame.a.prop.captain;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.CaptainPo;
import cn.xgame.gen.dto.MysqlGen.PropsDto;

/**
 * 舰长属性
 * @author deng		
 * @date 2015-6-17 下午7:19:24
 */
public class CaptainAttr extends IProp {

	private final CaptainPo templet;
	
	// 舰长品质
	private byte quality;

	public CaptainAttr( int uid, int nid, int count ) {
		super( uid, nid, count );
		templet = CsvGen.getCaptainPo(nid);
	}

	public CaptainAttr( PropsDto o ) {
		super( o );
		templet = CsvGen.getCaptainPo( getNid() );
	}
	
	@Override
	public IProp clone() {
		CaptainAttr ret = new CaptainAttr( getUid(), getNid(), getCount() );
		ret.quality 	= this.quality;
		return ret ;
	}
	
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
	
	public byte getQuality() {
		return quality;
	}
	public void setQuality(byte quality) {
		this.quality = quality;
	}



}
