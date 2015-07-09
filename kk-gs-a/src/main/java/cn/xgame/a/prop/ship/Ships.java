package cn.xgame.a.prop.ship;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Ship;
import cn.xgame.gen.dto.MysqlGen.PropsDto;

/**
 * 舰船对象
 * @author deng		
 * @date 2015-6-18 下午1:48:46
 */
public class Ships extends IProp{

	private final Ship templet;
	
	public Ships(int uid, int nid, int count) {
		super(uid, nid, count);
		templet = CsvGen.getShip(nid);
	}

	public Ships( PropsDto o ){
		super(o);
		// 下面加自己的属性 o.getAttach();
		templet = CsvGen.getShip( getnId() );
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
	
	public Ship templet(){ return templet; }
	@Override
	public PropType type() { return PropType.SHIP; }







}
