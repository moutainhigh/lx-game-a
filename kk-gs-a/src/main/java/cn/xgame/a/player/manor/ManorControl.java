package cn.xgame.a.player.manor;

import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import x.javaplus.collections.Lists;
import x.javaplus.util.Resources;
import x.javaplus.util.lua.Lua;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.manor.classes.BuildingType;
import cn.xgame.a.player.manor.classes.IBuilding;
import cn.xgame.a.player.manor.info.BaseBuilding;
import cn.xgame.a.player.manor.info.Building;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ReclaimPo;
import cn.xgame.system.LXConstants;
import cn.xgame.utils.Logs;

/**
 * 玩家领地 操作中心
 * @author deng		
 * @date 2015-6-23 下午12:35:43
 */
public class ManorControl implements IArrayStream,ITransformStream{

	// 领土
	private ReclaimPo territory = null;
	
	// 基地塔
	private BaseBuilding bbuild = null;
	
	// 建筑列表
	private List<IBuilding> builds = Lists.newArrayList();
	
	
	public ManorControl(Player player) {
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		builds.clear();
		ByteBuf buff = Unpooled.copiedBuffer(data);
		territory = CsvGen.getReclaimPo( buff.readInt() );
		bbuild = new BaseBuilding( buff.readInt() );
		bbuild.wrapBuffer(buff);
		BuildingType[] values = BuildingType.values();
		byte size = buff.readByte();
		for( int i = 0; i < size; i++ ){
			BuildingType type = values[buff.readByte()];
			int id 			= buff.readInt();
			IBuilding build = null;
			if( type == BuildingType.IMBAU ){
				build = new IBuilding(id);
			} else {
				build = new Building(id);
			}
			build.setType(type);
			build.wrapBuffer(buff);
			builds.add(build);
		}
	}

	@Override
	public byte[] toBytes() {
		if( territory == null ) return null;
		ByteBuf buff = Unpooled.buffer();
		buff.writeInt( territory.id );
		buff.writeInt( bbuild.templet().id );
		bbuild.putBuffer(buff);
		buff.writeByte( builds.size() );
		for( IBuilding build : builds ){
			buff.writeByte( build.getType().ordinal() );
			buff.writeInt( build.templet().id );
			build.putBuffer( buff );
		}
		return buff.array();
	}

	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		buffer.writeInt( getNid() );
		if( territory != null ){
			bbuild.buildTransformStream(buffer);
			buffer.writeByte( builds.size() );
			for( IBuilding o : builds ){
				o.buildTransformStream(buffer);
			}
		}
	}

	/**
	 * 更新一下 每个建筑
	 */
	public void update() {
		List<IBuilding> destroys = Lists.newArrayList();
		List<IBuilding> inservice = Lists.newArrayList();
		for( IBuilding o : builds ){
			if( !o.isComplete() )
				continue;
			switch (o.getType()) {
			case INSERVICE:
				((Building)o).update();
				break;
			case IMBAU:
				destroys.add(o);
				inservice.add( new Building( o ) );
				break;
			case UPGRADE:
				o.upgradeToNext();
				break;
			case DESTROY:
				destroys.add(o);
				break;
			}
		}
		builds.removeAll(destroys);
		builds.addAll(inservice);
	}
	
	/**
	 * 获取有产出的建筑列表
	 * @return
	 */
	public List<Building> getProduceBuildings() {
		List<Building> ret = Lists.newArrayList();
		for( IBuilding o : builds ){
			if( o.getType() == BuildingType.INSERVICE ){
				ret.add( (Building) o );
			}
		}
		return ret;
	}
	
	/**
	 * 通过建筑位置获取建筑信息
	 * @param index
	 * @return
	 */
	public IBuilding getBuildByIndex(byte index) {
		for( IBuilding o : builds ){
			if( o.getIndex() == index )
				return o;
		}
		return null;
	}
	
	/**
	 * 添加一个建筑到列表
	 * @param building
	 */
	public void addBuilding( IBuilding building ) {
		builds.add( building );
	}

	public ReclaimPo getTerritory() {
		return territory;
	}
	public void setTerritory(ReclaimPo territory) {
		if( this.territory == null ){
			//第一次创建 基地塔
			bbuild = new BaseBuilding( LXConstants.BASE_BUILD_ID );
			bbuild.setIndex((byte) 1);
		}
		this.territory = territory;
	}
	public List<IBuilding> getBuilds() {
		return builds;
	}
	public BaseBuilding getBbuild() {
		return bbuild;
	}
	/** 领地ID */
	public int getNid() {
		return territory == null ? 0 : territory.id;
	}

	public static void main(String[] args) {
		PropertyConfigurator.configureAndWatch( Resources.getResource("log4j.properties") );
		Lua.setLogClass(Logs.class);
		CsvGen.load();
		
		Building building = new Building( 20001 );
		building.setIndex((byte) 1);
		building.setType( BuildingType.INSERVICE );
		building.setEndtime( (int) (System.currentTimeMillis()/1000 ) );
		ManorControl as = new ManorControl(null);
		as.addBuilding(building);
		as.update();
	}


}
