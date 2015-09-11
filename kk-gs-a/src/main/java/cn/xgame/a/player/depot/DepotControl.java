package cn.xgame.a.player.depot;


import java.util.List;

import cn.xgame.a.IFromDB;
import cn.xgame.a.player.depot.o.StarDepot;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.gen.dto.MysqlGen.PropsDao;
import cn.xgame.gen.dto.MysqlGen.PropsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.system.SystemCfg;

import x.javaplus.collections.Lists;
import x.javaplus.mysql.db.Condition;

/**
 * 玩家所有道具 操作中心
 * @author deng		
 * @date 2015-6-17 下午6:56:27
 */
public class DepotControl implements IFromDB{

	private Player root;
	private List<StarDepot> depots = Lists.newArrayList();
	
	public DepotControl( Player player ) {
		this.root = player;
	}
	
	/**
	 * 根据星球ID 获取对应仓库
	 * @param beSnid
	 * @return
	 */
	public StarDepot getDepot( int beSnid ) {
		for( StarDepot depot : depots ){
			if( depot.getBeSnid() == beSnid )
				return depot;
		}
		StarDepot ret = new StarDepot( root, beSnid );
		depots.add(ret);
		return ret;
	}
	
	/**
	 * 获取所有星球的仓库道具
	 * @return
	 */
	public List<IProp> getAll() {
		List<IProp> ret = Lists.newArrayList();
		for( StarDepot depot : depots )
			ret.addAll( depot.getAll() );
		return ret;
	}
	
	@Override
	public void fromDB() {
		depots.clear();
		PropsDao dao = SqlUtil.getPropsDao();
		String sql = new Condition( PropsDto.gsidChangeSql( SystemCfg.ID ) ).AND( PropsDto.unameChangeSql( root.getUID() ) ).toString();
		List<PropsDto> dtos = dao.getByExact(sql);
		dao.commit();
		for( PropsDto dto : dtos ){
			StarDepot depot 	= getDepot( dto.getBeSnid() );
			IProp prop 			= IProp.create( dto );
			depot.append( prop );
		}
	}
	
}
