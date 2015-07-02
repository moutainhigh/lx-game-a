package cn.xgame.a.prop.captain;

import x.javaplus.mysql.db.Condition;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Captain;
import cn.xgame.gen.dto.MysqlGen.M_captainDao;
import cn.xgame.gen.dto.MysqlGen.M_captainDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;

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

	@Override
	public IProp clone() {
		Captains ret = new Captains( getuId(), getnId(), getCount() );
		return ret ;
	}
	
	/**
	 * 从数据库获取
	 * @param o
	 */
	public static Captains wrapDB( M_captainDto o ) {
		Captains ret = new Captains( o.getUid(), o.getNid(), o.getCount() );
		return ret;
	}
	
	@Override
	public void createDB(Player player) {
		M_captainDao dao = SqlUtil.getM_captainDao();
		M_captainDto dto = dao.create();
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
		M_captainDao dao = SqlUtil.getM_captainDao();
		String sql = new Condition( M_captainDto.uidChangeSql( getuId() ) ).AND( M_captainDto.gsidChangeSql( player.getGsid() ) ).
				AND( M_captainDto.unameChangeSql( player.getUID() ) ).toString();
		M_captainDto dto = dao.updateByExact( sql );
		dto.setNid( getnId() );
		dto.setCount( getCount() );
		///--下面加上属于自己的东西
		
		dao.commit(dto);
	}
	
	@Override
	public void deleteDB(Player player) {
		M_captainDao dao = SqlUtil.getM_captainDao();
		String sql = new Condition( M_captainDto.uidChangeSql( getuId() ) ).AND( M_captainDto.gsidChangeSql( player.getGsid() ) ).
				AND( M_captainDto.unameChangeSql( player.getUID() ) ).toString();
		dao.deleteByExact( sql );
		dao.commit();
	}
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		
	}

	public Captain templet(){ return templet; }
	
	@Override
	public PropType type() { return PropType.CAPTAIN; }


}
