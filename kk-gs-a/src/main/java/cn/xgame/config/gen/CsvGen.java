package cn.xgame.config.gen;import java.util.List;import java.util.Map;import x.javaplus.collections.Lists;import x.javaplus.csv.util.Csv;import cn.xgame.a.system.SystemCfg;import cn.xgame.config.o.ASD;
import cn.xgame.config.o.Item;

public class CsvGen {	public static final List<ASD> asds = Lists.newArrayList();	public static final List<Item> items = Lists.newArrayList();
	public static void load(){		loadASD( "ASD.csv" );		loadItem( "item.csv" );
	}	private static void loadASD( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "/" + file );				for( Map<String, String> data : csv.getValues() ){			ASD o = new ASD(data);			asds.add( o );		}	}	private static void loadItem( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "/" + file );				for( Map<String, String> data : csv.getValues() ){			Item o = new Item(data);			items.add( o );		}	}
	public static ASD getASD( int x ){		for( ASD o : asds ){			if( o.ID == x )				return o;		}		return null;	}	public static Item getItem( int x ){		for( Item o : items ){			if( o.ID == x )				return o;		}		return null;	}
}