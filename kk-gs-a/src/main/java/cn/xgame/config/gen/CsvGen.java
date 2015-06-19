package cn.xgame.config.gen;

import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.Util.File;

import cn.xgame.config.o.PlanetMap;

public class CsvGen {
	
	private static final List<PlanetMap> data = Lists.newArrayList();
	//C:\Users\dell_\Desktop
	
	public static void load(){
		try {
			List<String> content = File.getLines( "C:/Users/dell_/Desktop/PlanetMap.csv" );
			String[] head = content.get(1).split(",");
			
			for( int i = 0; i < head.length; i++ )
				System.out.println( head[i] );
			
			
			for( String s : content )
				System.out.println( s );
			
		} catch (Exception e) {
		}
	}
	
	
	
	public static PlanetMap get( short id ){
		for( PlanetMap o : data ){
			if( o.id == id )
				return o;
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		CsvGen.load();
	}
}
