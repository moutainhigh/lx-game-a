package cn.xgame.config;

import x.javaplus.csv.App;

public class Build {

	public static void main(String[] args) {
	
		String[] ags = new String[] { 
				"-filePath", "C:/Users/dell_/Desktop",
				"-oPath", "src/main/java/cn/xgame/config/o",
				"-genPath", "src/main/java/cn/xgame/config/gen",
				"-cPackageName", "cn.xgame.config.gen",
				"-oPackageName", "cn.xgame.config.o" 
				};
		
		App.generateConfigGen( ags );
	}
}
