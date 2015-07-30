package cn.xgame.utils;

import cn.xgame.system.SystemCfg;
import x.javaplus.util.lua.Lua;

/**
 * 调用外边lua文件 工具
 * @author deng		
 * @date 2015-7-30 上午11:40:46
 */
public class LuaUtil {

	/**
	 * 游戏数据相关
	 * @return
	 */
	public static Lua getGameData() {
		return new Lua( SystemCfg.FILE_NAME + "lua/gameData.lua" );
	}

	/**
	 * 副本其他相关
	 * @return
	 */
	public static Lua getEctype() {
		return new Lua( SystemCfg.FILE_NAME + "lua/ectype.lua" );
	}

	/**
	 * 副本战斗
	 * @return
	 */
	public static Lua getEctypeCombat() {
		return new Lua( SystemCfg.FILE_NAME + "lua/ectypeCombat.lua" );
	}

}
