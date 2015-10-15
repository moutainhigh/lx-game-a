package cn.xgame.a.player.manor;

import java.util.Iterator;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.manor.classes.BuildingType;
import cn.xgame.a.player.manor.classes.IBuilding;
import cn.xgame.a.player.manor.info.Building;
import cn.xgame.a.player.manor.info.UnBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ReclaimcapacityPo;

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
		byte size = buff.readByte();
		for( int i = 0; i < size; i++ ){
			IBuilding build = new IBuilding( buff.readInt() );
			build.setIndex( buff.readByte() );
			builds.add(build);
		}
	}

	@Override
	public byte[] toBytes() {
		if( territory == null ) return null;
		ByteBuf buff = Unpooled.buffer();
		buff.writeInt( territory.id );
		buff.writeByte( builds.size() );
		for( IBuilding build : builds ){
			buff.writeInt( build.templet().id );
			buff.writeByte( build.getIndex() );
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
		Iterator<IBuilding> iter = builds.iterator();
		List<IBuilding> destroys = Lists.newArrayList();
		while( iter.hasNext() ){
			IBuilding o = iter.next();
			if( !o.isComplete() )
				continue;
			switch (o.getType()) {
			case INSERVICE:
				((Building)o).update();
				break;
			case IMBAU:
				o = new Building( o );
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
	public boolean isCanBuild( UnBuilding building ) throws Exception {
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

}
