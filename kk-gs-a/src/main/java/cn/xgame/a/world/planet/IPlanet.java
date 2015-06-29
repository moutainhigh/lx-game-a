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
	private SpecialtyControl specialtys = new SpecialtyControl();
	
	// 星球资源
	private ResourceControl depots = new ResourceControl();
	
	// 星球建筑
	private BuildingControl buildings = new BuildingControl();
	
	
	public IPlanet( Stars clone ){
		templet = clone;
	}
	
	/**
	 * 初始化 并且保存数据库
	 * @param dto
	 */
	public void init( PlanetDataDto dto ) {
		maxSpace = templet.room;
		specialtys.fromTemplet( templet.goods );
		buildings.fromTemplet( templet.building );
		// 下面保存 到数据库
		dto.setId( templet.id );
		dto.setMaxSpace( maxSpace );
		dto.setBuildings( buildings.toBytes() );
		dto.setSpecialtys( specialtys.toBytes() );
	}
	
	/**
	 * 从数据库获取数据
	 * @param dto
	 */
	public void wrap( PlanetDataDto dto ){
		maxSpace = dto.getMaxSpace();
		specialtys.fromBytes( dto.getSpecialtys() );
		depots.fromBytes( dto.getDepots() );
		buildings.fromBytes( dto.getBuildings() );
	}
	
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeShort( getId() );
		buffer.writeShort( maxSpace );
		specialtys.buildTransformStream( buffer );
		buildings.buildTransformStream( buffer );
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
	public SpecialtyControl getSpecialtys() {
		return specialtys;
	}
	public void setSpecialtys(SpecialtyControl specialtys) {
		this.specialtys = specialtys;
	}
	public ResourceControl getDepots() {
		return depots;
	}
	public void setDepots(ResourceControl depots) {
		this.depots = depots;
	}
	public BuildingControl getBuildings() {
		return buildings;
	}
	public void setBuildings(BuildingControl buildings) {
		this.buildings = buildings;
	}


}
