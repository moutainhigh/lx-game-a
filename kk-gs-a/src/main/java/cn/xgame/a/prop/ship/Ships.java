package cn.xgame.a.prop.ship;

import x.javaplus.mysql.db.Condition;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Ship;
import cn.xgame.gen.dto.MysqlGen.M_shipDto;
import cn.xgame.gen.dto.MysqlGen.M_shipDao;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;

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
	public void createDB(Player player) {
		M_shipDao dao = SqlUtil.getM_shipDao();
		M_shipDto dto = dao.create();
		dto.setGsid( player.getGsid() );
		dto.setUname( player.getUID() );
		dto.setUid( getuId() );
		dto.setNid( getnId() );
		dto.setCount( getCount() );
		///--下面加上属于自己的东西
		
		dao.commit(dto);
	}

	@Override
	public void updateDB(Player player) {
		M_shipDao dao 	= SqlUtil.getM_shipDao();
		String sql 		= new Condition( M_shipDto.uidChangeSql( getuId() ) ).AND( M_shipDto.gsidChangeSql( player.getGsid() ) ).
				AND( M_shipDto.unameChangeSql( player.getUID() ) ).toString();
		M_shipDto dto 	= dao.updateByExact( sql );
		dto.setNid( getnId() );
		dto.setCount( getCount() );
		///--下面加上属于自己的东西
		
		dao.commit(dto);
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}
	
	public Ship templet(){ return templet; }
	@Override
	public PropType type() { return PropType.SHIP; }

}
