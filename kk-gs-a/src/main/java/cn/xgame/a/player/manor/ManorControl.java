package cn.xgame.a.player.manor;

import java.util.Iterator;
import java.util.List;


import x.javaplus.collections.Lists;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.xgame.a.IArrayStream;
import cn.xgame.a.player.manor.classes.BStatus;
import cn.xgame.a.player.manor.classes.BType;
import cn.xgame.a.player.manor.info.IBuilding;
import cn.xgame.a.player.manor.info.ProduceBuilding;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.BbuildingPo;
import cn.xgame.config.o.ReclaimPo;

/**
 * 玩家领地 操作中心
 * @author deng		
 * @date 2015-6-23 下午12:35:43
 */
public class ManorControl implements IArrayStream{

	// 领土
	private ReclaimPo territory = null;
	
	// 建筑列表
	private List<IBuilding> builds = Lists.newArrayList();
	
	
	public ManorControl(Player player) {
	}

	@Override
	public void fromBytes(byte[] data) {
		if( data == null ) return;
		builds.clear();
		ByteBuf buff = Unpooled.copiedBuffer(data);
		// 领地信息
		territory = CsvGen.getReclaimPo( buff.readInt() );
		// 其他建筑
		BType[] values = BType.values();
		byte size = buff.readByte();
		for( int i = 0; i < size; i++ ){
			BbuildingPo templet = CsvGen.getBbuildingPo(buff.readInt());
			BType type = values[templet.buildtype-1];
			IBuilding o = type.create(templet);
			o.wrapBuffer(buff);
			builds.add(o);
		}
	}

	@Override
	public byte[] toBytes() {
		if( territory == null ) return null;
		ByteBuf buff = Unpooled.buffer();
		buff.writeInt( territory.id );
		buff.writeByte( builds.size() );
		for( IBuilding build : builds ){
			buff.writeInt( build.getNid() );
			build.putBuffer( buff );
		}
		return buff.array();
	}

	/**
	 * 更新一下 每个建筑
	 */
	public void update() {
		Iterator<IBuilding> iter = builds.iterator();
		while( iter.hasNext() ){
			IBuilding o = iter.next();
			if( !o.isComplete() )
				continue;
			switch (o.getStatus()) {
			case INSERVICE:
				{
					o.update();
				}
				break;
			case IMBAU:
				{
					o.build();
				}
				break;
			case UPGRADE:
				{
					o.upgradeToNext();
				}
				break;
			case DESTROY:
				{
					iter.remove();
				}
				break;
			}
		}
	}
	
	/**
	 * 获取有产出的建筑列表
	 * @return
	 */
	public List<ProduceBuilding> getProduceBuildings() {
		List<ProduceBuilding> ret = Lists.newArrayList();
		for( IBuilding o : builds ){
			if( o.getStatus() == BStatus.INSERVICE && o.getType() == BType.PRODUCE ){
				ProduceBuilding x = (ProduceBuilding) o;
				if( x.isEmpty() )
					continue;
				ret.add( x );
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
//		PropertyConfigurator.configureAndWatch( Resources.getResource("log4j.properties") );
//		Lua.setLogClass(Logs.class);
//		CsvGen.load();
//		
//		ProduceBuilding building = new ProduceBuilding( 20001 );
//		building.setIndex((byte) 1);
//		building.setStatus( BStatus.INSERVICE );
//		building.setEndtime( (int) (System.currentTimeMillis()/1000 ) );
//		ManorControl as = new ManorControl(null);
//		as.addBuilding(building);
//		as.update();
	}


}
