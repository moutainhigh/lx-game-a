package cn.xgame.a.prop.captain;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Captain;
import cn.xgame.gen.dto.MysqlGen.PropsDto;

/**
 * 舰长对象
 * @author deng		
 * @date 2015-6-17 下午7:19:24
 */
public class Captains extends IProp {

	private final Captain templet;

	public Captains( int uid, int nid, int count ) {
		super( uid, nid, count );
		templet = CsvGen.getCaptain(nid);
	}

	public Captains( PropsDto o ) {
		super( o );
		templet = CsvGen.getCaptain( getnId() );
	}
	
	@Override
	public IProp clone() {
		Captains ret = new Captains( getuId(), getnId(), getCount() );
		return ret ;
	}
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		
	}

	public Captain templet(){ return templet; }
	
	@Override
	public PropType type() { return PropType.CAPTAIN; }

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


}
