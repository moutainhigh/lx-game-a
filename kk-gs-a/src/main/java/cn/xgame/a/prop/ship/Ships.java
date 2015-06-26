package cn.xgame.a.prop.ship;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Ship;
import cn.xgame.gen.dto.MysqlGen.M_shipDto;

/**
 * 舰船对象
 * @author deng		
 * @date 2015-6-18 下午1:48:46
 */
public class Ships extends IProp{

	private final Ship templet;
	
	/**
	 * 从数据库获取
	 * @param o
	 */
	public static Ships wrapDB( M_shipDto o ) {
		Ships ret = new Ships( o.getUid(), o.getNid(), o.getCount() );
		return ret;
	}
	
	public Ships(int uid, int nid, int count) {
		super(uid, nid, count);
		templet = CsvGen.getShip((short) nid);
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PropType type() {
		return PropType.SHIP;
	}

	@Override
	public void createDB(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDB(Player player) {
		// TODO Auto-generated method stub
		
	}
	
	
	public Ship templet(){ return templet; }


}
