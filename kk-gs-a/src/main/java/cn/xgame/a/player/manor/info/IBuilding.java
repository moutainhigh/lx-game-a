package cn.xgame.a.player.manor.info;

import io.netty.buffer.ByteBuf;
import cn.xgame.a.IBufferStream;
import cn.xgame.a.player.manor.classes.BStatus;
import cn.xgame.a.player.manor.classes.BType;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.BbuildingPo;
import cn.xgame.utils.Logs;

/**
 * 玩家领地建筑基类
 * @author deng		
 * @date 2015-10-15 上午11:19:30
 */
public abstract class IBuilding implements IBufferStream{
	
	// 建筑类型
	private final BType type;
	// 建筑等级
	private byte level;
	// 建筑升级到下一个等级的表格ID
	private int nextId;
	// 建筑占用格子数
	private byte usegrid;
	// 是否可以销毁
	private boolean isDestroy;
	
	
	// 建筑表格ID
	private int nid;
	// 建筑物位置
	private byte index;
	// 建筑状态  1.已建筑 2.建筑中 3.升级中 4.销毁中
	private BStatus status;
	// 结束时间 对应各个类型的时间
	private int endtime;
	
	
	public IBuilding( BType type, BbuildingPo templet ){
		this.type 		= type;
		this.nid 		= templet.id;
		this.level 		= templet.level;
		this.nextId 	= templet.next;
		this.usegrid 	= templet.usegrid;
		this.isDestroy  = templet.removed == 1;
	}
	
	
	@Override
	public void putBuffer(ByteBuf buf) {
		buf.writeByte( index );
		buf.writeByte( status.ordinal() );
		buf.writeInt( endtime );
	}
	@Override
	public void wrapBuffer(ByteBuf buf) {
		index	= buf.readByte();
		status 	= BStatus.values()[buf.readByte()];
		endtime	= buf.readInt();
	}
	
	/**
	 * 刷新
	 */
	public abstract void update();
	
	/**
	 * 初始数据
	 */
	public abstract void init();
	
	
	/**
	 * 建筑完成
	 */
	public void build() {
		setEndtime( 0 );
		setStatus(BStatus.INSERVICE);
		init();
	}

	/**
	 * 升级为下一级
	 */
	public void upgradeToNext() {
		setEndtime( 0 );
		setStatus(BStatus.INSERVICE);
		// 下面写过数据
		BbuildingPo templet = CsvGen.getBbuildingPo( getNextId() );
		if( templet != null ){
			nid 	= templet.id;
			nextId 	= templet.next;
			init();
		}else{
			Logs.error( "领地建筑 "+getNid()+" 升级出错 at="+getNextId()+" 在表格没有找到" );
		}
	}
	
	/**
	 * 该位置 是否重叠
	 * @param index
	 * @param room
	 * @return
	 */
	public boolean indexIsOverlap( byte oindex, byte oroom ) {
		return oindex < index+usegrid && oindex+oroom > index;
	}
	
	/**
	 * 时间是否完成
	 * @return
	 */
	public boolean isComplete() {
		return (int)(System.currentTimeMillis()/1000) >= endtime;
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
	public BStatus getStatus() {
		return status;
	}
	public void setStatus(BStatus status) {
		this.status = status;
	}
	public BType getType() {
		return type;
	}
	public byte getLevel() {
		return level;
	}
	public int getNid() {
		return nid;
	}
	public int getNextId() {
		return nextId;
	}
	public byte getUsegrid() {
		return usegrid;
	}
	public boolean isDestroy() {
		return isDestroy;
	}
}
