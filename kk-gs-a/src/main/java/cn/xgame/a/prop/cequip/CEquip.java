package cn.xgame.a.prop.cequip;

import x.javaplus.mysql.db.Condition;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Weapon;
import cn.xgame.gen.dto.MysqlGen.M_cequipDto;
import cn.xgame.gen.dto.MysqlGen.M_cequipDao;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;

/**
 * 舰长装备对象
 * @author deng		
 * @date 2015-6-18 下午1:49:59
 */
public class CEquip extends IProp{

	private final Weapon templet;
	
	/**
	 * 从数据库获取
	 * @param o
	 */
	public static CEquip wrapDB( M_cequipDto o ) {
		CEquip ret = new CEquip( o.getUid(), o.getNid(), o.getCount() );
		return ret;
	}
	
	public CEquip(int uid, int nid, int count) {
		super(uid, nid, count);
		templet = CsvGen.getWeapon(nid);
	}

	@Override
	public void createDB(Player player) {
		M_cequipDao dao = SqlUtil.getM_cequipDao();
		M_cequipDto dto = dao.create();
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
		M_cequipDao dao 	= SqlUtil.getM_cequipDao();
		String sql 		= new Condition( M_cequipDto.uidChangeSql( getuId() ) ).AND( M_cequipDto.gsidChangeSql( player.getGsid() ) ).
				AND( M_cequipDto.unameChangeSql( player.getUID() ) ).toString();
		M_cequipDto dto 	= dao.updateByExact( sql );
		dto.setNid( getnId() );
		dto.setCount( getCount() );
		///--下面加上属于自己的东西
		
		dao.commit(dto);
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}
	
	public Weapon templet() { return templet; }
	@Override
	public PropType type() { return PropType.CEQUIP; }
}
