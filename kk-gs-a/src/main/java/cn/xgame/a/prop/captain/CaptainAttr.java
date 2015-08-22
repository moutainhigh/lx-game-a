package cn.xgame.a.prop.captain;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
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
		return ret ;
	}
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		
	}

	public CaptainPo templet(){ return templet; }
	
	@Override
	public void createDB(Player player) {
		super.create(player, null);
	}

	@Override
	public void updateDB(Player player) {
		super.update(player, null);
	}

	@Override
	public void putAttachBuffer(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void wrapAttach(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}
	public byte getQuality() {
		return quality;
	}
	public void setQuality(byte quality) {
		this.quality = quality;
	}

}
