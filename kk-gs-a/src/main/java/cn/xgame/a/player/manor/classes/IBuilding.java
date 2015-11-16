package cn.xgame.a.player.manor.classes;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import x.javaplus.collections.Lists;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.ITransformStream;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.BbuildingPo;

/**
 * 玩家领地建筑基类
 * @author deng		
 * @date 2015-10-15 上午11:19:30
 */
public class IBuilding implements ITransformStream,IBufferStream{

	private BbuildingPo templet ;
	// 产出列表模板
	private List<Goods> produceTemplet = Lists.newArrayList();
	// 产出模板总比例
	private float sumScale = 0;
	
	
	// 建筑物位置
	private byte index;
	
	// 建筑类型  1.已建筑 2.建筑中 3.升级中 4.销毁中
	private BuildingType type;
	
	// 结束时间 对应各个类型的时间
	private int endtime;
	
	
	public IBuilding( int id ){
		templet = CsvGen.getBbuildingPo(id);
		initProduceTemplet();
	}
	// 初始化产出模板
	private void initProduceTemplet() {
		if( templet == null || templet.ProduceTpye.isEmpty() )
			return;
		produceTemplet.clear();
		String[] array = templet.ProduceTpye.split( "\\|" );
		for( String str : array ){
			Goods goods = new Goods();
			String[] x	= str.split(";");
			goods.setId( Integer.parseInt( x[0] ) );
			goods.addCount( Float.parseFloat( x[1] ) );
			produceTemplet.add(goods);
			sumScale	+= goods.getCount();
		}
		// 排个序  个数从小到大
		Collections.sort( produceTemplet, new Comparator<Goods>() {
			@Override
			public int compare( Goods o1, Goods o2 ) {
				return (int) (o1.getCount() - o2.getCount());
			}
		} );
	}
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeByte( index );
		buf.writeInt( endtime );
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		index	= buf.readByte();
		endtime	= buf.readInt();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( templet.id );
		buffer.writeByte( index );
		buffer.writeByte( type.ordinal() );
		buffer.writeInt( endtime );
	}
	
	/** 
	 * 建造中
	 */
	public void inBuild() {
		setType( BuildingType.IMBAU );
		setEndtime( (int)(System.currentTimeMillis()/1000) + templet.needtime );
	}
	
	/**
	 * 升级中
	 */
	public void inUpgrade() {
		setType( BuildingType.UPGRADE );
		setEndtime( (int)(System.currentTimeMillis()/1000) + templet.needtime );
		produceTemplet.clear();
	}
	
	/**
	 * 销毁中
	 */
	public void inDestroy() {
		setType( BuildingType.DESTROY );
		setEndtime( (int)(System.currentTimeMillis()/1000) + (templet.needtime/10) );
		produceTemplet.clear();
	}
	
	/**
	 * 升级为下一级
	 */
	public void upgradeToNext() {
		templet = CsvGen.getBbuildingPo( templet.next );
		initProduceTemplet();
		setType( BuildingType.INSERVICE );
		setEndtime( (int)(System.currentTimeMillis()/1000) );
	}
	
	/**
	 * 该位置 是否重叠
	 * @param index
	 * @param room
	 * @return
	 */
	public boolean indexIsOverlap( byte oindex, byte oroom ) {
		return oindex < index+templet.usegrid && oindex+oroom > index;
	}
	
	/**
	 * 是否可以升级
	 * @return
	 */
	public boolean isUpgrade() {
		return templet.next != 0;
	}
	
	/**
	 * 是否可以销毁
	 * @return
	 */
	public boolean isDestroy() {
		return templet.removed == 1;
	}
	
	/**
	 * 时间是否完成
	 * @return
	 */
	public boolean isComplete() {
		return (int)(System.currentTimeMillis()/1000) >= endtime;
	}
	
	
	public BbuildingPo templet() {
		return templet;
	}
	public List<Goods> getProduceTemplet() {
		return produceTemplet;
	}
	public float getSumScale() {
		return sumScale;
	}
	public byte getIndex() {
		return index;
	}
	public void setIndex(byte index) {
		this.index = index;
	}
	public int getEndtime() {
		return endtime;
	}
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	public BuildingType getType() {
		return type;
	}
	public void setType(BuildingType type) {
		this.type = type;
	}

	
}
