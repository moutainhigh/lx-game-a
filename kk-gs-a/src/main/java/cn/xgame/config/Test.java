package cn.xgame.config;

import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Item;

public class Test {
	public static void main(String[] args) {
//		Item o = CsvGen.getItem( 1001 );
		CsvGen.load();
		
		for( Item item : CsvGen.items ){
			System.out.println( item.ID + ", " + item.name + ", " + item.manymax + ", " + item.defcolor );
		}
		
		Item o = CsvGen.getItem( 11001 );
		System.out.println( o.manymax );
		
	}
}
