package cn.xgame.a.player.captain.o;

import x.javaplus.mysql.db.Condition;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.IUObject;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Captain;
import cn.xgame.gen.dto.MysqlGen.CaptainsDao;
import cn.xgame.gen.dto.MysqlGen.CaptainsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;

/**
 * 一个 舰长 信息
 * @author deng		
 * @date 2015-7-9 下午12:28:55
 */
public class CaptainInfo extends IUObject implements ITransformStream{

	private final Captain template; 
	
	public CaptainInfo(int uid, int nid) {
		super( uid, nid );
		template = CsvGen.getCaptain(nid);
	}

	public CaptainInfo(CaptainsDto dto) {
		super( dto.getUid(), dto.getNid() );
		template = CsvGen.getCaptain(dto.getNid());
	}

	public Captain template(){ return template; }
	
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( getuId() );
		buffer.writeInt( getnId() );
	}

	
	//TODO------------数据库相关
	public void createDB( Player root ) {
		CaptainsDao dao = SqlUtil.getCaptainsDao();
		CaptainsDto dto = dao.create();
		dto.setGsid( root.getGsid() );
		dto.setUname( root.getUID() );
		dto.setUid( getuId() );
		setDBData( dto );
		dao.commit(dto);
	}
	public void updateDB( Player player ) {
		CaptainsDao dao = SqlUtil.getCaptainsDao();
		String sql 	= new Condition( CaptainsDto.gsidChangeSql( player.getGsid() ) ).
				AND( CaptainsDto.unameChangeSql( player.getUID() ) ).AND( CaptainsDto.uidChangeSql( getuId() ) ).toString();
		CaptainsDto dto = dao.updateByExact( sql );
		setDBData(dto);
		dao.commit(dto);
	}
	private void setDBData(CaptainsDto dto) {
		dto.setNid(getnId());
	}
}
