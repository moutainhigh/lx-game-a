package cn.xgame.a.world.planet;

import java.util.List;

import x.javaplus.collections.Lists;


import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.ectype.info.ChapterInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.planet.data.building.BuildingControl;
import cn.xgame.a.world.planet.data.resource.ResourceControl;
import cn.xgame.a.world.planet.data.specialty.SpecialtyControl;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.a.world.planet.home.o.Institution;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ChapterPo;
import cn.xgame.config.o.StarsPo;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDto;
import cn.xgame.system.LXConstants;
import cn.xgame.utils.Logs;

/**
 * 星球基类
 * @author deng		
 * @date 2015-6-25 下午4:32:24
 */
public abstract class IPlanet implements ITransformStream{

	// 星球配置表
	protected final StarsPo templet;
	
	// 星球总空间
	protected short 				maxSpace;
	
	// 星球特产
	protected SpecialtyControl 		specialtyControl ;
	
	// 星球资源
	protected ResourceControl 		depotControl ;
	
	// 星球建筑
	protected BuildingControl 		buildingControl ;
	
	// 副本章节列表
	private List<ChapterInfo> 		chapters = Lists.newArrayList();
	
	public IPlanet( StarsPo clone ){
		templet 			= clone;
		specialtyControl 	= new SpecialtyControl( getId() );
		depotControl 		= new ResourceControl( getId() );
		buildingControl 	= new BuildingControl( getId() );
	}
	
	/**
	 * 初始化 并且保存数据库
	 * @param dto
	 */
	public void init( PlanetDataDto dto ) {
		maxSpace = templet.room;
		specialtyControl.fromTemplet( templet.goods );
		buildingControl.fromTemplet( templet.building );
		initChapters( templet.ectypes );
		// 下面保存 到数据库
		dto.setId( templet.id );
		dto.setMaxSpace( maxSpace );
		dto.setBuildings( buildingControl.toBytes() );
		dto.setSpecialtys( specialtyControl.toBytes() );
	}
	
	/**
	 * 从数据库获取数据
	 * @param dto
	 */
	public void wrap( PlanetDataDto dto ){
		maxSpace = dto.getMaxSpace();
		specialtyControl.fromBytes( dto.getSpecialtys() );
		specialtyControl.fromTemplet( templet.goods );
		depotControl.fromBytes( dto.getDepots() );
		buildingControl.fromBytes( dto.getBuildings() );
		initChapters( templet.ectypes );
	}
	
	// 初始化副本章节信息
	private void initChapters( String ectypes ) {
		String[] array = ectypes.split(";");
		for( String x : array ){
			ChapterPo ctemplet = CsvGen.getChapterPo( Integer.parseInt( x ) );
			if( ctemplet == null ) {
				Logs.error( "在生成偶发副本出错 Chapter表格找不到 " + x + ", at = IPlanet.initChapters" );
				continue;
			}
			ChapterInfo chapter = new ChapterInfo( ctemplet, getId() );
			chapter.init( ctemplet );
			chapters.add(chapter);
		}
	}
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeInt( getId() );
		buffer.writeShort( maxSpace );
	}
	
	/** 保存数据库 */
	public abstract void updateDB();
	/** 获取瞭望的星球ID列表 */
	public abstract List<Integer> getScopePlanet();

	/** 发起 建筑 投票 */
	public void sponsorBuivote( Player player, int nid, byte index, int time ) throws Exception { }
	/** 参与 建筑 投票*/
	public void participateBuildVote(Player player, int nid, byte isAgree) throws Exception { }
	/** 发起 驱逐元老 投票 */
	public void sponsorGenrVote( Player player, String uid, String explain ) throws Exception { }
	/** 参与 驱逐元老 投票 */
	public void participateGenrVote( Player player, String uid,byte isAgree ) throws Exception { }
	/** 申请所有政务数据  */
	public void putAlllAffair( Player player, ByteBuf response) { }
	/** 申请所有元老数据 */
	public List<Child> getAllGenrs() { return null; }
	/** 捐献资源 */
	public void donateResource( Player player, IProp prop ){}
	/** 获得该星球体制 */
	public Institution getInstitution() { return null; }
	/** 获得该星人数 */
	public int getPeople() { return 0; }
	/** 获得该星科技等级 */
	public byte getTechLevel() { return 0; }
	
	
	public StarsPo templet(){ 
		return templet; 
	}
	public int getId() { 
		return templet.id; 
	}
	public short getMaxSpace() { 
		return maxSpace; 
	}
	public SpecialtyControl getSpecialtyControl() { 
		return specialtyControl; 
	}
	public ResourceControl getDepotControl() { 
		return depotControl; 
	}
	public BuildingControl getBuildingControl() { 
		return buildingControl; 
	}
	public List<ChapterInfo> getChapters() {
		return chapters;
	}
	
	
	/**
	 * 获取星球的酒馆更新时间  单位秒
	 * @return
	 */
	public int getTavernUpdateTime() {
		// TODO 这里暂时 写死 到时候根据星球的普惠 改变值
		return LXConstants.TAVERN_UPDATE_TIME;
	}

	/**
	 * 根据章节ID 获取对应章节
	 * @param id
	 * @return
	 */
	public ChapterInfo getChapter( int id ){
		for( ChapterInfo chapter : chapters ){
			if( chapter.getId() == id )
				return chapter;
		}
		return null;
	}
	
}
