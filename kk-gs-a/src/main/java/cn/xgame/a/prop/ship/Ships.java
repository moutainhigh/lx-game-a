package cn.xgame.a.prop.ship;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ShipPo;
import cn.xgame.gen.dto.MysqlGen.PropsDto;

/**
 * 舰船对象
 * @author deng		
 * @date 2015-6-18 下午1:48:46
 */
public class Ships extends IProp{

	private final ShipPo templet;
	
	public Ships(int uid, int nid, int count) {
		super(uid, nid, count);
		templet = CsvGen.getShipPo(nid);
	}

	public Ships( PropsDto o ){
		super(o);
		templet = CsvGen.getShipPo( getnId() );
		// 下面加自己的属性 o.getAttach();
	}
	
	@Override
	public IProp clone() {
		Ships ret = new Ships( getuId(), getnId(), getCount());
		return ret;
	}
	
	@Override
	public void createDB( Player player ) {
		super.create( player, null );
	}

	@Override
	public void updateDB( Player player ) {
		super.update( player, null );
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}
	
	public ShipPo templet(){ return templet; }

	@Override
	public void putAttachBuffer(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wrapAttach(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}







}
