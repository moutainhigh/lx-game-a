package cn.xgame.a.world.planet;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
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
	private final Stars templet;
	
	// 星球总空间
	private short maxSpace;
	
	// 星球特产
	private SpecialtyControl specialtyControl = new SpecialtyControl();
	
	// 星球资源
	private ResourceControl depotControl = new ResourceControl();
	
	// 星球建筑
	private BuildingControl buildingControl = new BuildingControl();
	
	
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
		dto.setBuildings( getBuildingControl().toBytes() );
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
