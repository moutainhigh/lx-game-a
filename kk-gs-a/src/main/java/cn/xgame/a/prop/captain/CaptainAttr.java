package cn.xgame.a.prop.captain;

import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.Quality;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.CaptainPo;
import cn.xgame.config.o.ItemPo;
import cn.xgame.utils.LuaUtil;

/**
 * 舰长属性
 * @author deng		
 * @date 2015-6-17 下午7:19:24
 */
public class CaptainAttr extends IProp {

	private final CaptainPo templet;
	
	// 亲密度
	private int curIntimacy = 0;
	
	// 周薪
	private int weekpay;
	
	// 操控
	private int control;
	
	// 感知
	private int perception;
	
	// 亲和力
	private int affinity;
	
	
	public CaptainAttr( ItemPo item, int uid, int nid, int count, Quality quality ) {
		super( item, uid, nid, count, quality );
		templet = CsvGen.getCaptainPo(nid);
	}

	private CaptainAttr( CaptainAttr clone ) {
		super( clone );
		templet 	= clone.templet;
		curIntimacy = clone.curIntimacy;
		weekpay 	= clone.weekpay;
		control 	= clone.control;
		perception 	= clone.perception;
		affinity 	= clone.affinity;
	}
	
	@Override
	public CaptainAttr clone() { return new CaptainAttr( this ); }
	
	public CaptainPo templet(){ return templet; }
	
	@Override
	public byte[] toAttachBytes() {
		ByteBuf buf = Unpooled.buffer( 20 );
		buildTransformStream( buf );
		return buf.array();
	}

	@Override
	public void wrapAttachBytes( byte[] bytes ) {
		if( bytes == null ) return;
		ByteBuf buf = Unpooled.copiedBuffer(bytes);
		curIntimacy = buf.readInt();
		weekpay 	= buf.readInt();
		control 	= buf.readInt();
		perception 	= buf.readInt();
		affinity 	= buf.readInt();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( curIntimacy );
		buffer.writeInt( weekpay );
		buffer.writeInt( control );
		buffer.writeInt( perception );
		buffer.writeInt( affinity );
	}

	@Override
	public void randomAttachAttr() {
		Lua lua = LuaUtil.getGameData();
		LuaValue[] value = lua.getField( "randomAttachAttr" ).call( 4, templet, type().toNumber(), getQuality().toNumber() );
		curIntimacy	= 0;
		weekpay 	= value[0].getInt();
		control 	= value[1].getInt();
		perception 	= value[2].getInt();
		affinity 	= value[3].getInt();
	}

	public int getCurIntimacy() {
		return curIntimacy;
	}
	public void setCurIntimacy(int curIntimacy) {
		this.curIntimacy = curIntimacy;
	}
	public int getWeekpay() {
		return weekpay;
	}
	public void setWeekpay(int weekpay) {
		this.weekpay = weekpay;
	}
	public int getControl() {
		return control;
	}
	public void setControl(int control) {
		this.control = control;
	}
	public int getPerception() {
		return perception;
	}
	public void setPerception(int perception) {
		this.perception = perception;
	}
	public int getAffinity() {
		return affinity;
	}
	public void setAffinity(int affinity) {
		this.affinity = affinity;
	}
	

}
