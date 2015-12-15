package cn.xgame.a.player.u.classes;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.lua.Lua;
import cn.xgame.a.player.u.Player;
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
		// 初始化任务
		List<Integer> tasks = Lists.newArrayList();
		tasks.add( 99001 );
		ret.getTasks().addCanTask( tasks );
		// 调用lua脚本
		Lua lua = LuaUtil.getInit();
		lua.getField( "createPlayerData" ).call( 0, ret );
		
		return ret;
	}

}
