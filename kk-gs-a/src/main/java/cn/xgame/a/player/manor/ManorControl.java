package cn.xgame.a.player.manor;

import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;
import x.javaplus.util.Resources;
import x.javaplus.util.lua.Lua;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.manor.classes.BuildingType;
import cn.xgame.a.player.manor.classes.IBuilding;
import cn.xgame.a.player.manor.info.Building;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ReclaimcapacityPo;
import cn.xgame.utils.Logs;

/**
 * 玩家领地 操作中心
 * @author deng		
 * @date 2015-6-23 下午12:35:43
 */
public class ManorControl implements IArrayStream,ITransformStream{

	// 领土
	private ReclaimcapacityPo territory = null;
	
	// 建筑列表
	private List<IBuilding> builds = Lists.newArrayList();
	
	
	public ManorControl(Player player) {
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		builds.clear();
		ByteBuf buff = Unpooled.copiedBuffer(data);
		territory = CsvGen.getReclaimcapacityPo( buff.readInt() );
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
//		update();
	}

	@Override
	public byte[] toBytes() {
		if( territory == null ) return null;
//		update();
		ByteBuf buff = Unpooled.buffer();
		buff.writeInt( territory.id );
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
		buffer.writeByte( builds.size() );
		for( IBuilding o : builds ){
			o.buildTransformStream(buffer);
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

	/**
	 * 检测这个建筑是否可以建造
	 * @param building
	 * @return
	 * @throws Exception 
	 */
	public boolean isCanBuild( IBuilding building ) throws Exception {
		update();
		int curGrid = building.templet().usegrid;
		// 判断位置
		for( IBuilding o : builds ){
			if( o.indexIsOverlap( building.getIndex(), building.templet().usegrid ) )
				throw new Exception( ErrorCode.INDEX_OCCUPY.name() );
			curGrid += o.templet().usegrid;
		}
		// 判断该领地的格子数还够不够
		if( curGrid > territory.room )
			throw new Exception( ErrorCode.ROOM_LAZYWEIGHT.name() );
		return true;
	}

	public ReclaimcapacityPo getTerritory() {
		return territory;
	}
	public void setTerritory(ReclaimcapacityPo territory) {
		this.territory = territory;
	}
	public List<IBuilding> getBuilds() {
		return builds;
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
