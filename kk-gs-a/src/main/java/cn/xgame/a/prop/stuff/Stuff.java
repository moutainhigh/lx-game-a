package cn.xgame.a.prop.stuff;


import x.javaplus.mysql.db.Condition;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.gen.dto.MysqlGen.StuffDao;
import cn.xgame.gen.dto.MysqlGen.StuffDto;

/**
 * 材料对象
 * @author deng		
 * @date 2015-6-17 下午7:46:35
 */
public class Stuff extends IProp{
	
	@Override
	public PropType type() {
		return PropType.STUFF;
	}
	
	/**
	 * 从数据库获取
	 * @param o
	 */
	public Stuff( StuffDto o ) {
		setuId( o.getUid() );
		setnId( o.getNid() );
		setCount( o.getCount() );
	}

	/**
	 * 创建一个 并保存到数据库
	 * @param uid
	 * @param nid
	 * @param count
	 */
	public Stuff( int uid, int nid, int count ) {
		initialize( uid, nid, count );
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
//		toBuffer( buffer );
	}

	
	@Override
	public void createDB( Player player ) {
		StuffDao dao = SqlUtil.getStuffDao();
		StuffDto dto = dao.create();
		dto.setGsid( player.getGsid() );
		dto.setUname( player.getUID() );
		dto.setUid( getuId() );
		dto.setNid( getnId() );
		dto.setCount( getCount() );
		dao.commit(dto);
	}
	
	@Override
	public void updateDB( Player player ) {
		StuffDao dao 	= SqlUtil.getStuffDao();
		String sql 		= new Condition( StuffDto.uidChangeSql( getuId() ) ).AND( StuffDto.gsidChangeSql( player.getGsid() ) ).
				AND( StuffDto.unameChangeSql( player.getUID() ) ).toString();
		StuffDto dto 	= dao.updateByExact( sql );
		dto.setNid( getnId() );
		dto.setCount( getCount() );
		dao.commit(dto);
	}


}
