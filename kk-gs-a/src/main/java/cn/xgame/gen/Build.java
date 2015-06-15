package cn.xgame.gen;

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
				"-packageName", "cn.xgame.gen.dto", "asdsad" };
		App.generateMysqlGen( ags );
	}
	
	private static void generateMysql() {
		App.generateMysql( "src/main/java/cn/xgame/gen/o" );
	}
	
}
