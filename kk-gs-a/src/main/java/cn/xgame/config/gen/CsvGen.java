package cn.xgame.config.gen;import java.util.List;import java.util.Map;import x.javaplus.collections.Lists;import x.javaplus.csv.util.Csv;import cn.xgame.system.SystemCfg;import cn.xgame.config.o.AnswerPo;
import cn.xgame.config.o.AskingPo;
import cn.xgame.config.o.BbuildingPo;
import cn.xgame.config.o.CaptainPo;
import cn.xgame.config.o.ChapterPo;
import cn.xgame.config.o.EctypePo;
import cn.xgame.config.o.ItemPo;
import cn.xgame.config.o.NoticePo;
import cn.xgame.config.o.ReclaimcapacityPo;
import cn.xgame.config.o.RquestionPo;
import cn.xgame.config.o.SbuildingPo;
import cn.xgame.config.o.ShipPo;
import cn.xgame.config.o.StarsPo;
import cn.xgame.config.o.TaskPo;
import cn.xgame.config.o.TaskcndPo;
import cn.xgame.config.o.TaverndataPo;
import cn.xgame.config.o.TechPo;
import cn.xgame.config.o.TreasurePo;
import cn.xgame.config.o.WeaponPo;

public class CsvGen {	public static final List<AnswerPo> answerpos = Lists.newArrayList();	public static final List<AskingPo> askingpos = Lists.newArrayList();	public static final List<BbuildingPo> bbuildingpos = Lists.newArrayList();	public static final List<CaptainPo> captainpos = Lists.newArrayList();	public static final List<ChapterPo> chapterpos = Lists.newArrayList();	public static final List<EctypePo> ectypepos = Lists.newArrayList();	public static final List<ItemPo> itempos = Lists.newArrayList();	public static final List<NoticePo> noticepos = Lists.newArrayList();	public static final List<ReclaimcapacityPo> reclaimcapacitypos = Lists.newArrayList();	public static final List<RquestionPo> rquestionpos = Lists.newArrayList();	public static final List<SbuildingPo> sbuildingpos = Lists.newArrayList();	public static final List<ShipPo> shippos = Lists.newArrayList();	public static final List<StarsPo> starspos = Lists.newArrayList();	public static final List<TaskPo> taskpos = Lists.newArrayList();	public static final List<TaskcndPo> taskcndpos = Lists.newArrayList();	public static final List<TaverndataPo> taverndatapos = Lists.newArrayList();	public static final List<TechPo> techpos = Lists.newArrayList();	public static final List<TreasurePo> treasurepos = Lists.newArrayList();	public static final List<WeaponPo> weaponpos = Lists.newArrayList();
	public static void load(){		loadAnswerPo( "answer.csv" );		loadAskingPo( "asking.csv" );		loadBbuildingPo( "bbuilding.csv" );		loadCaptainPo( "captain.csv" );		loadChapterPo( "chapter.csv" );		loadEctypePo( "ectype.csv" );		loadItemPo( "item.csv" );		loadNoticePo( "notice.csv" );		loadReclaimcapacityPo( "reclaimcapacity.csv" );		loadRquestionPo( "rquestion.csv" );		loadSbuildingPo( "sbuilding.csv" );		loadShipPo( "ship.csv" );		loadStarsPo( "stars.csv" );		loadTaskPo( "task.csv" );		loadTaskcndPo( "taskcnd.csv" );		loadTaverndataPo( "taverndata.csv" );		loadTechPo( "tech.csv" );		loadTreasurePo( "treasure.csv" );		loadWeaponPo( "weapon.csv" );
	}	private static void loadAnswerPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			AnswerPo o = new AnswerPo(data);			answerpos.add( o );		}	}	private static void loadAskingPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			AskingPo o = new AskingPo(data);			askingpos.add( o );		}	}	private static void loadBbuildingPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			BbuildingPo o = new BbuildingPo(data);			bbuildingpos.add( o );		}	}	private static void loadCaptainPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			CaptainPo o = new CaptainPo(data);			captainpos.add( o );		}	}	private static void loadChapterPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			ChapterPo o = new ChapterPo(data);			chapterpos.add( o );		}	}	private static void loadEctypePo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			EctypePo o = new EctypePo(data);			ectypepos.add( o );		}	}	private static void loadItemPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			ItemPo o = new ItemPo(data);			itempos.add( o );		}	}	private static void loadNoticePo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			NoticePo o = new NoticePo(data);			noticepos.add( o );		}	}	private static void loadReclaimcapacityPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			ReclaimcapacityPo o = new ReclaimcapacityPo(data);			reclaimcapacitypos.add( o );		}	}	private static void loadRquestionPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			RquestionPo o = new RquestionPo(data);			rquestionpos.add( o );		}	}	private static void loadSbuildingPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			SbuildingPo o = new SbuildingPo(data);			sbuildingpos.add( o );		}	}	private static void loadShipPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			ShipPo o = new ShipPo(data);			shippos.add( o );		}	}	private static void loadStarsPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			StarsPo o = new StarsPo(data);			starspos.add( o );		}	}	private static void loadTaskPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			TaskPo o = new TaskPo(data);			taskpos.add( o );		}	}	private static void loadTaskcndPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			TaskcndPo o = new TaskcndPo(data);			taskcndpos.add( o );		}	}	private static void loadTaverndataPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			TaverndataPo o = new TaverndataPo(data);			taverndatapos.add( o );		}	}	private static void loadTechPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			TechPo o = new TechPo(data);			techpos.add( o );		}	}	private static void loadTreasurePo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			TreasurePo o = new TreasurePo(data);			treasurepos.add( o );		}	}	private static void loadWeaponPo( String file ){				Csv csv = new Csv( SystemCfg.FILE_NAME + "configs/" + file );				for( Map<String, String> data : csv.getValues() ){			WeaponPo o = new WeaponPo(data);			weaponpos.add( o );		}	}
	public static AnswerPo getAnswerPo( int x ){		for( AnswerPo o : answerpos ){			if( o.id == x )				return o;		}		return null;	}	public static AskingPo getAskingPo( int x ){		for( AskingPo o : askingpos ){			if( o.id == x )				return o;		}		return null;	}	public static BbuildingPo getBbuildingPo( int x ){		for( BbuildingPo o : bbuildingpos ){			if( o.id == x )				return o;		}		return null;	}	public static CaptainPo getCaptainPo( int x ){		for( CaptainPo o : captainpos ){			if( o.id == x )				return o;		}		return null;	}	public static ChapterPo getChapterPo( int x ){		for( ChapterPo o : chapterpos ){			if( o.id == x )				return o;		}		return null;	}	public static EctypePo getEctypePo( int x ){		for( EctypePo o : ectypepos ){			if( o.id == x )				return o;		}		return null;	}	public static ItemPo getItemPo( int x ){		for( ItemPo o : itempos ){			if( o.id == x )				return o;		}		return null;	}	public static NoticePo getNoticePo( int x ){		for( NoticePo o : noticepos ){			if( o.id == x )				return o;		}		return null;	}	public static ReclaimcapacityPo getReclaimcapacityPo( int x ){		for( ReclaimcapacityPo o : reclaimcapacitypos ){			if( o.id == x )				return o;		}		return null;	}	public static RquestionPo getRquestionPo( int x ){		for( RquestionPo o : rquestionpos ){			if( o.ID == x )				return o;		}		return null;	}	public static SbuildingPo getSbuildingPo( int x ){		for( SbuildingPo o : sbuildingpos ){			if( o.id == x )				return o;		}		return null;	}	public static ShipPo getShipPo( int x ){		for( ShipPo o : shippos ){			if( o.id == x )				return o;		}		return null;	}	public static StarsPo getStarsPo( int x ){		for( StarsPo o : starspos ){			if( o.id == x )				return o;		}		return null;	}	public static TaskPo getTaskPo( int x ){		for( TaskPo o : taskpos ){			if( o.id == x )				return o;		}		return null;	}	public static TaskcndPo getTaskcndPo( int x ){		for( TaskcndPo o : taskcndpos ){			if( o.id == x )				return o;		}		return null;	}	public static TaverndataPo getTaverndataPo( int x ){		for( TaverndataPo o : taverndatapos ){			if( o.id == x )				return o;		}		return null;	}	public static TechPo getTechPo( int x ){		for( TechPo o : techpos ){			if( o.id == x )				return o;		}		return null;	}	public static TreasurePo getTreasurePo( int x ){		for( TreasurePo o : treasurepos ){			if( o.id == x )				return o;		}		return null;	}	public static WeaponPo getWeaponPo( int x ){		for( WeaponPo o : weaponpos ){			if( o.id == x )				return o;		}		return null;	}
}