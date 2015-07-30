package cn.xgame.a.player.captain;

import io.netty.buffer.ByteBuf;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.mysql.db.Condition;
import cn.xgame.a.IFromDB;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.captain.o.CaptainInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.gen.dto.MysqlGen.CaptainsDao;
import cn.xgame.gen.dto.MysqlGen.CaptainsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.system.SystemCfg;

/**
 * 舰长室 操作类
 * @author deng		
 * @date 2015-7-9 下午12:28:12
 */
public class CaptainsControl implements ITransformStream,IFromDB{

	private Player root;
	
	// 舰长 列表
	private List<CaptainInfo> captains = Lists.newArrayList();
	
	public CaptainsControl( Player player ){
		this.root = player;
	}

	public List<CaptainInfo> getAll() {
		return captains;
	}
	
	@Override
	public void fromDB() {
		captains.clear();
		CaptainsDao dao = SqlUtil.getCaptainsDao();
		String sql = new Condition( CaptainsDto.gsidChangeSql( SystemCfg.ID ) ).AND( CaptainsDto.unameChangeSql( root.getUID() ) ).toString();
		List<CaptainsDto> dtos = dao.getByExact( sql );
		dao.commit();
		for( CaptainsDto dto : dtos ){
			CaptainInfo ship = new CaptainInfo( dto );
			captains.add(ship);
		}
	}
	
	/** 保存所有数据 到数据库 */
	public void update() {
		for( CaptainInfo captain : captains )
			captain.updateDB( root );
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeByte( captains.size() );
		for( CaptainInfo captain : captains ){
			captain.buildTransformStream(buffer);
		}
	}
	

	private void append( CaptainInfo captain ) {
		captains.add(captain);
	}
	
	/**
	 * 根据唯一ID 获取舰长
	 * @param uid
	 * @return
	 */
	public CaptainInfo getCaptain( int uid ) {
		for( CaptainInfo captain : captains ){
			if( captain.getuId() == uid )
				return captain;
		}
		return null;
	}

	/**
	 * 创建一个 舰长
	 * @param nid
	 */
	public CaptainInfo createCaptain( int nid ) {
		
		CaptainInfo cap = new CaptainInfo( root.generatorCaptainUID(), nid );
		
		append(cap);
		
		// 在数据库创建数据
		cap.createDB( root );
		
		return cap;
	}



}
