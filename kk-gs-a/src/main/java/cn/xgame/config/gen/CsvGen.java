package cn.xgame.config.gen;import java.util.List;import java.util.Map;import x.javaplus.collections.Lists;import x.javaplus.csv.util.Csv;import cn.xgame.a.system.SystemCfg;import cn.xgame.config.o.Item;
public class CsvGen {	public static final List<Item> items = Lists.newArrayList();
	public static void load(){		loadItem( "item.csv" );
	}	private static void loadItem( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "/" + file );				for( Map<String, String> data : csv.getValues() ){			Item o = new Item(data);			items.add( o );		}	}
	public static Item getItem( int x ){		for( Item o : items ){			if( o.ID == x )				return o;		}		return null;	}
}