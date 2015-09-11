package cn.xgame.utils;

import cn.xgame.config.gen.CsvGen;
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
		Lua lua = new Lua( SystemCfg.FILE_NAME + "lua/GameData.lua" );
		lua.registerObject( CsvGen.class, "CsvGen" );
		return lua;
	}

	/**
	 * 副本其他相关
	 * @return
	 */
	public static Lua getEctypeInfo() {
		return new Lua( SystemCfg.FILE_NAME + "lua/EctypeInfo.lua" );
	}

	/**
	 * 副本战斗
	 * @return
	 */
	public static Lua getEctypeCombat() {
		Lua lua = new Lua( SystemCfg.FILE_NAME + "lua/EctypeCombat.lua" );
		lua.registerObject( CsvGen.class, "CsvGen" );
		return lua;
	}

	/**
	 * 舰长属性
	 * @return
	 */
	public static Lua getCaptainProperty() {
		return new Lua( SystemCfg.FILE_NAME + "lua/CaptainProperty.lua" );
	}

	/**
	 * 数据库 buffer 输入数据
	 * @return
	 */
	public static Lua getDatabaseBufferForm() {
		return new Lua( SystemCfg.FILE_NAME + "lua/DatabaseBufferForm.lua" );
	}
	
}
