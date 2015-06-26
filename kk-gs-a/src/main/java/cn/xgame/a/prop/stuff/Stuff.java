package cn.xgame.a.prop.stuff;


import x.javaplus.mysql.db.Condition;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.PropType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Material;
import cn.xgame.gen.dto.MysqlGen.M_stuffDao;
import cn.xgame.gen.dto.MysqlGen.M_stuffDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;

/**
 * 材料对象
 * @author deng		
 * @date 2015-6-17 下午7:46:35
 */
public class Stuff extends IProp{
	
	private final Material templet;
	
	
	@Override
	public PropType type() {
		return PropType.STUFF;
	}
	
	/**
	 * 从数据库获取
	 * @param o
	 */
	public static Stuff wrapDB( M_stuffDto o ) {
		Stuff ret = new Stuff( o.getUid(), o.getNid(), o.getCount() );
		return ret;
	}

	/**
	 * 创建一个 并保存到数据库
	 * @param uid
	 * @param nid
	 * @param count
	 */
	public Stuff( int uid, int nid, int count ) {
		super( uid, nid, count );
		templet = CsvGen.getMaterial( nid );
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
//		toBuffer( buffer );
	}

	
	@Override
	public void createDB( Player player ) {
		M_stuffDao dao = SqlUtil.getM_stuffDao();
		M_stuffDto dto = dao.create();
		dto.setGsid( player.getGsid() );
		dto.setUname( player.getUID() );
		dto.setUid( getuId() );
		dto.setNid( getnId() );
		dto.setCount( getCount() );
		dao.commit(dto);
	}
	
	@Override
	public void updateDB( Player player ) {
		M_stuffDao dao 	= SqlUtil.getM_stuffDao();
		String sql 		= new Condition( M_stuffDto.uidChangeSql( getuId() ) ).AND( M_stuffDto.gsidChangeSql( player.getGsid() ) ).
				AND( M_stuffDto.unameChangeSql( player.getUID() ) ).toString();
		M_stuffDto dto 	= dao.updateByExact( sql );
		dto.setNid( getnId() );
		dto.setCount( getCount() );
		dao.commit(dto);
	}


	public Material templet(){ return templet; }
}
