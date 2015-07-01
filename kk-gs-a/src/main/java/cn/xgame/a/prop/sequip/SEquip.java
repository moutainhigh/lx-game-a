package cn.xgame.a.prop.sequip;

import x.javaplus.mysql.db.Condition;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.gen.dto.MysqlGen.M_sequipDto;
import cn.xgame.gen.dto.MysqlGen.M_sequipDao;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;

/**
 * 舰船装备对象
 * @author deng		
 * @date 2015-6-18 下午1:49:18
 */
public class SEquip extends IProp{

	/**
	 * 从数据库获取
	 * @param o
	 */
	public static SEquip wrapDB( M_sequipDto o ) {
		SEquip ret = new SEquip( o.getUid(), o.getNid(), o.getCount() );
		return ret;
	}
	
	public SEquip(int uid, int nid, int count) {
		super(uid, nid, count);
	}

	@Override
	public void createDB(Player player) {
		M_sequipDao dao = SqlUtil.getM_sequipDao();
		M_sequipDto dto = dao.create();
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
		M_sequipDao dao = SqlUtil.getM_sequipDao();
		String sql = new Condition( M_sequipDto.uidChangeSql( getuId() ) ).AND( M_sequipDto.gsidChangeSql( player.getGsid() ) ).
				AND( M_sequipDto.unameChangeSql( player.getUID() ) ).toString();
		M_sequipDto dto = dao.updateByExact( sql );
		dto.setNid( getnId() );
		dto.setCount( getCount() );
		///--下面加上属于自己的东西
		
		dao.commit(dto);
	}
	
	@Override
	public void deleteDB(Player player) {
		M_sequipDao dao = SqlUtil.getM_sequipDao();
		String sql = new Condition( M_sequipDto.uidChangeSql( getuId() ) ).AND( M_sequipDto.gsidChangeSql( player.getGsid() ) ).
				AND( M_sequipDto.unameChangeSql( player.getUID() ) ).toString();
		dao.deleteByExact(sql);
		dao.commit();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public PropType type() { return PropType.SEQUIP; }


	
}
