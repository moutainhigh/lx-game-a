package cn.xgame.a.world.planet;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.planet.data.building.BuildingControl;
import cn.xgame.a.world.planet.data.resource.ResourceControl;
import cn.xgame.a.world.planet.data.specialty.SpecialtyControl;
import cn.xgame.config.o.Stars;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDto;

/**
 * 星球基类
 * @author deng		
 * @date 2015-6-25 下午4:32:24
 */
public abstract class IPlanet implements ITransformStream{

	// 星球配置表
	protected final Stars templet;
	
	// 星球总空间
	protected short maxSpace;
	
	// 星球特产
	protected SpecialtyControl specialtyControl = new SpecialtyControl();
	
	// 星球资源
	protected ResourceControl depotControl = new ResourceControl();
	
	// 星球建筑
	protected BuildingControl buildingControl = new BuildingControl();
	
	
	public IPlanet( Stars clone ){
		templet = clone;
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
		depotControl.fromBytes( dto.getDepots() );
		buildingControl.fromBytes( dto.getBuildings() );
	}
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeShort( getId() );
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
	public void sponsorGenrVote( Player player, String uid, int time ) throws Exception { }
	/** 参与 驱逐元老 投票 */
	public void participateGenrVote( Player player, String uid,byte isAgree ) throws Exception { }
	
	/** 申请所有政务数据 */
	public void putAlllAffair(ByteBuf response) { }
	/** 捐献资源 */
	public void donateResource( Player player, IProp prop ){}
	/** 是否可以捐献 */
	public abstract boolean isCanDonate();
	/** 保存数据库 */
	public abstract void updateDB();
	public Stars templet(){ return templet; }
	public Short getId() { return templet.id; }
	public short getMaxSpace() {
		return maxSpace;
	}
	public void setMaxSpace(short maxSpace) {
		this.maxSpace = maxSpace;
	}
	public SpecialtyControl getSpecialtyControl() {
		return specialtyControl;
	}
	public void setSpecialtyControl(SpecialtyControl specialtys) {
		this.specialtyControl = specialtys;
	}
	public ResourceControl getDepotControl() {
		return depotControl;
	}
	public void setDepotControl(ResourceControl depots) {
		this.depotControl = depots;
	}
	public BuildingControl getBuildingControl() {
		return buildingControl;
	}
	public void setBuildingControl(BuildingControl buildingControl) {
		this.buildingControl = buildingControl;
	}



}
