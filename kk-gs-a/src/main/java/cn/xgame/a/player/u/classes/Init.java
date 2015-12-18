package cn.xgame.a.player.u.classes;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.lua.Lua;
import cn.xgame.a.player.manor.ManorControl;
import cn.xgame.a.player.manor.classes.BType;
import cn.xgame.a.player.manor.info.BaseBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.BbuildingPo;
import cn.xgame.config.o.ReclaimPo;
import cn.xgame.system.LXConstants;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

/**
 * 玩家初始化
 * @author deng		
 * @date 2015-7-9 下午12:58:57
 */
public class Init {

	/**
	 * 玩家初始化
	 * @param uID 玩家唯一ID
	 * @param headIco 头像ID
	 * @param adjutantId 副官ID
	 * @param name 玩家名字
	 * @param countryId 分配的星球ID
	 * @return
	 */
	public static Player run( String uID, int headIco, int adjutantId, String name, int countryId ) {
		
		Player ret = new Player( uID, headIco, name );
		ret.setAdjutantId( adjutantId );
		ret.setCountryId( countryId );
		
		//------------------ 初始化任务
		List<Integer> tasks = Lists.newArrayList();
		tasks.add( 99001 );
		ret.getTasks().addCanTask( tasks );
		
		//------------------ 初始化领地
		ManorControl manors = ret.getManors();
		ReclaimPo reclaim = CsvGen.getReclaimPo(2);
		// 设置领土
		manors.setTerritory(reclaim);
		// 下面默认添加一个基地建筑
		BbuildingPo templet = CsvGen.getBbuildingPo( LXConstants.BASE_BUILD_ID );
		if( templet != null ){
			BaseBuilding building = new BaseBuilding(BType.BASE, templet);
			building.setIndex((byte) 1);
			building.build();
			manors.addBuilding(building);
		}else{
			Logs.error( "玩家第一次购买领地 创建基地建筑失败 at="+LXConstants.BASE_BUILD_ID+" 在表格没有找到" );
		}
		
		//------------------ 调用lua脚本
		Lua lua = LuaUtil.getInit();
		lua.getField( "createPlayerData" ).call( 0, ret );
		
		return ret;
	}
}
