package cn.xgame.gen;

import cn.xgame.system.SystemCfg;
import x.javaplus.mysql.App;


public class Build {

	public static void main(String[] args) {
		
		generateMysqlGen();
		
		generateMysql();
		
		System.out.println( "generate finish" );
	}
	
	private static void generateMysqlGen() {
		String[] ags = new String[] { 
				"-dtoPath", "src/main/java/cn/xgame/gen/o",
				"-dstPath", "src/main/java/cn/xgame/gen/dto",
				"-packageName", "cn.xgame.gen.dto" };
		App.generateMysqlGen( SystemCfg.getDatabaseName(), ags );
	}
	
	private static void generateMysql() {
		App.generateMysql( SystemCfg.getDatabaseName(), "src/main/java/cn/xgame/gen/o" );
	}
	
}
