package cn.xgame.utils;

import cn.xgame.a.system.SystemCfg;
import x.javaplus.util.lua.Lua;

public class LuaUtil {

	public static Lua getGameData() {
		return new Lua( SystemCfg.FILE_NAME + "lua/gameData.lua" );
	}

}
