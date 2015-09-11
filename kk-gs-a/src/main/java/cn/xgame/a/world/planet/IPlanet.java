package cn.xgame.a.world.planet;

import java.util.List;

import x.javaplus.collections.Lists;


import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.ectype.o.ChapterEctype;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.planet.data.building.BuildingControl;
import cn.xgame.a.world.planet.data.resource.ResourceControl;
import cn.xgame.a.world.planet.data.specialty.SpecialtyControl;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.a.world.planet.home.o.Institution;
import cn.xgame.config.o.StarsPo;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDto;

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
	}
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeInt( getId() );
		buffer.writeShort( maxSpace );
	}

	/** 发起 建筑 投票 */
	public void sponsorBuivote( Player player, int nid, byte index, int time ) throws Exception { }
	/** 参与 建筑 投票*/
	public void participateBuildVote(Player player, int nid, byte isAgree) throws Exception { }
	
	/** 发起 科技 投票 */
	public void sponsorTechVote( Player player, int nid, int time ) throws Exception { }
	/** 参与 科技 投票 */
	public void participateTechVote(Player player, int nid, byte isAgree) throws Exception { }
	
	/** 发起 驱逐元老 投票 */
	public void sponsorGenrVote( Player player, String uid, String explain ) throws Exception { }
	/** 参与 驱逐元老 投票 */
	public void participateGenrVote( Player player, String uid,byte isAgree ) throws Exception { }
	
	/** 申请所有政务数据 
	 * @param player */
	public void putAlllAffair( Player player, ByteBuf response) { }
	/** 申请所有元老数据 */
	public List<Child> getAllGenrs() { return null; }
	/** 捐献资源 */
	public void donateResource( Player player, IProp prop ){}
	
	/** 是否可以捐献 */
	public abstract boolean isCanDonate();
	/** 保存数据库 */
	public abstract void updateDB();
	/** 获取瞭望的星球ID列表 */
	public abstract List<Integer> getScopePlanet();
	
	/** 获得该星球体制 */
	public Institution getInstitution() { return null; }
	/** 获得该星人数 */
	public int getPeople() { return 0; }
	/** 获得该星科技等级 */
	public byte getTechLevel() { return 0; }
	
	public StarsPo templet(){ return templet; }
	public int getId() { return templet.id; }
	public short getMaxSpace() { return maxSpace; }
	public void setMaxSpace(short maxSpace) { this.maxSpace = maxSpace; }
	public SpecialtyControl getSpecialtyControl() { return specialtyControl; }
	public ResourceControl getDepotControl() { return depotControl; }
	public BuildingControl getBuildingControl() { return buildingControl; }

	/** 
	 * 获取该星球本身自带的副本
	 * @return
	 */
	public List<Integer> getItselfEctype() {
		List<Integer> ret = Lists.newArrayList();
		if( !templet().ectypes.isEmpty() ){
			String[] str = templet().ectypes.split(";");
			for( String o : str )
				ret.add( Integer.parseInt(o) );
		}
		return ret;
	}
	
	/**
	 * 获取偶发副本
	 * @return
	 */
	public List<ChapterEctype> getAccidentalEctype() {
		
//		Lua lua = LuaUtil.getEctype();
//		LuaValue[] ret = lua.getField( "getStarAccEctype" ).call( 1, templet.id );
//		String[] content = ret[0].getString().split(",");
//		
//		List<AccEctype> retAcc = Lists.newArrayList();
//		for( String v : content ){
//			if( v.isEmpty() ) continue;
//			EctypePo e = CsvGen.getEctypePo( Integer.parseInt(v) );
//			if( e == null ) continue;
//			AccEctype acc = new AccEctype(templet.id,e);
//			if( !acc.isClose() ){
//				retAcc.add( acc );
//			}
//		}
		return null;
	}


	






}
