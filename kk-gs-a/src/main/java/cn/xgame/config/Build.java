package cn.xgame.config;

import cn.xgame.system.SystemCfg;
import x.javaplus.csv.App;

public class Build {

	public static void main(String[] args) {
	
		generateConfigs();
		
//		CsvGen.load();
		
		System.out.println( "generate finish" );
	}

	private static void generateConfigs() {
		String[] ags = new String[] { 
				"-filePath", SystemCfg.FILE_NAME,
				"-oPath", "src/main/java/cn/xgame/config/o",
				"-genPath", "src/main/java/cn/xgame/config/gen",
				"-cPackageName", "cn.xgame.config.gen",
				"-oPackageName", "cn.xgame.config.o" 
				};
		
		App.generateConfigGen( ags );
	}
	
	
}
