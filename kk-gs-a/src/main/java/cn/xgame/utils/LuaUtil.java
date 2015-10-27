package cn.xgame.utils;

import cn.xgame.a.prop.IProp;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.system.SystemCfg;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;

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
	 * 副本评分
	 * @return
	 */
	public static Lua getEctypeGraded() {
		Lua lua = new Lua( SystemCfg.FILE_NAME + "lua/EctypeGraded.lua" );
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

	/**
	 * 初始数据
	 * @return
	 */
	public static Lua getInit() {
		return new Lua( SystemCfg.FILE_NAME + "lua/Init.lua" );
	}

	/**
	 * 星球商店
	 * @return
	 */
	public static Lua getStarshop() {
		Lua lua = new Lua( SystemCfg.FILE_NAME + "lua/Starshop.lua" );
		lua.registerObject( IProp.class, "IProp" );
		return lua;
	}
	
	/**
	 * 任务信息
	 * @return
	 */
	public static Lua getTaskInfo() {
		return new Lua( SystemCfg.FILE_NAME + "lua/TaskInfo.lua" );
	}
	
	public static void main(String[] args) {
		Lua.setLogClass(Logs.class);
		
		Lua lua = getTaskInfo();
		LuaValue[] value = lua.getField("generateTask").call( 1, 1001, 1 );
		
		String s = value[0].getString();
		
		System.out.println( s );
		
	}


	
}
