package cn.xgame.a.prop;

import x.javaplus.util.Util.Random;
import cn.xgame.a.ITransformStream;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ItemPo;
import cn.xgame.gen.dto.MysqlGen.PropsDto;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.system.LXConstants;
import io.netty.buffer.ByteBuf;

/**
 * 道具 基类
 * @author deng		
 * @date 2015-6-17 下午7:02:11
 */
public abstract class IProp implements ITransformStream{
	
	// 基础物品表
	private final ItemPo item;
	private final PropType type;
	
	// 唯一ID
	private int uid;
	// 表格ID
	private int nid;
	// 数量
	private int count;
	// 品质
	private Quality quality = Quality.COLOR00;
	
	/**
	 * 创建一个 并保存到数据库
	 * @param item 
	 * @param uid
	 * @param nid
	 * @param count
	 * @param quality 
	 */
	public IProp( ItemPo item, int uid, int nid, int count, Quality quality ){
		this.uid 		= uid;
		this.nid 		= nid;
		this.item 		= item;
		this.type 		= PropType.fromNumber( item.bagtype );
		addCount( count );
		this.quality	= quality;
	}
	
	/**
	 * 从buffer中获取数据
	 * @param o
	 */
	public IProp( ByteBuf buf ){
		this.uid 		= buf.readInt();
		this.nid 		= buf.readInt();
		this.item 		= CsvGen.getItemPo(nid);
		this.type 		= PropType.fromNumber( item.bagtype );
		addCount( buf.readInt() );
		this.quality	= Quality.fromNumber( buf.readByte() );
		wrapAttachBytes( RW.readBytes(buf) );
	}
	
	/**
	 * 拷贝一份
	 * @param src
	 */
	public IProp( IProp src ) {
		this.uid 		= src.uid;
		this.nid 		= src.nid;
		this.count 		= src.count;
		this.item 		= src.item;
		this.type 		= src.type;
		this.quality	= src.quality;
	}

	/**
	 * 创建一个简单的道具
	 * @param uid
	 * @param nid
	 * @param count
	 * @return
	 */
	public static IProp create( int uid, int nid, int count ) {
		ItemPo item 		= CsvGen.getItemPo(nid);
		return create( item, uid, nid, count, randomQuality( item.quality ) );
	}
	
	public static IProp create( int uid, int nid, int count, byte quality ) {
		return create( CsvGen.getItemPo(nid), uid, nid, count, Quality.fromNumber( quality ) );
	}
	public static IProp create( int uid, int nid, int count, Quality quality ) {
		return create( CsvGen.getItemPo(nid), uid, nid, count, quality );
	}
	private static IProp create( ItemPo item, int uid, int nid, int count, Quality quality ){
		PropType type = PropType.fromNumber( item.bagtype );
		return type.create( item, uid, nid, count, quality );
	}
	
	/**
	 * 从数据库中创建一个道具出来
	 * @param buf
	 * @return
	 */
	public static IProp create( PropsDto o ) {
		IProp prop = create( o.getUid(), o.getNid(), o.getCount(), o.getQuality() );
		prop.wrapAttachBytes( o.getAttach() );
		return prop;
	}
	
	/**
	 * 从buffer中创建一个道具出来
	 * @param buf
	 * @return
	 */
	public static IProp create( ByteBuf buf ) {
		int uid 	= buf.readInt();
		int nid 	= buf.readInt();
		int count 	= buf.readInt();
		byte q		= buf.readByte();
		IProp prop 	= create( uid, nid, count, q );
		prop.wrapAttachBytes( RW.readBytes(buf) );
		return prop;
	}
	
	/**
	 * 随机品质
	 * @param quality
	 * @return
	 */
	public static Quality randomQuality( String quality ) {
		String[] content 	= quality.split( "\\|" );
		for( String str : content ){
			String[] x 		= str.split( ";" );
			int rand		= Random.get( 0, 10000 );
			if( rand <= Integer.parseInt( x[1] ) )
				return Quality.fromNumber( Byte.parseByte( x[0] ) );
		}
		return Quality.COLOR01;
	}
	
	public String toString(){
		return type().name() + ",u=" + uid + ",n=" + nid + ",c=" + count + ",q=" + quality.toNumber(); 
	}
	
	/**
	 * 写入基础数据到buffer
	 * @param buffer
	 */
	public void putBaseBuffer( ByteBuf buffer ) {
		buffer.writeInt( uid );
		buffer.writeInt( nid );
		buffer.writeInt( count );
		buffer.writeByte( quality.toNumber() );
	}
	
	/**
	 * 写入全部数据到buffer - 只用于数据库保存
	 * @param buf
	 */
	public void putBuffer( ByteBuf buffer ) {
		putBaseBuffer( buffer );
		RW.writeBytes( buffer, toAttachBytes() );
	}
	
	/**
	 * 克隆一个
	 */
	public abstract IProp clone();
	
	/**
	 * 把附加属性塞入
	 * @param buf
	 */
	public abstract byte[] toAttachBytes();
	
	/**
	 * 获取 附加属性
	 * @param buf
	 */
	public abstract void wrapAttachBytes( byte[] bytes );
	
	/**
	 * 随机 生成附加属性
	 */
	public abstract void randomAttachAttr();
	
	
	public ItemPo item(){ return item; }
	public int getUid() { return uid; }
	public void setUid(int uId) { this.uid = uId; }
	public int getNid() { return nid; }
	public void setNid(int nId) { this.nid = nId; }
	public int getCount() { return count; }
	public void setCount(int count) { this.count = count; }
	public Quality getQuality() { return quality; }
	public void setQuality(Quality quality) { this.quality = quality; }
	
	/** 道具类型 */
	public PropType type(){ return type; }
	/**
	 * 道具小类型<br>
	 * ------type == 2 舰船<br>
	 * 1.<br>
	 * ------type == 3 舰船装备<br>
	 * 1.武器(攻击)<br>
	 * 2.防具(防御)<br>
	 * 3.推进器<br>
	 * 4.辅助<br>
	 * 
	 * @return
	 */
	public int itemType() { return item.itemtype; }
	
	/** 获取这个物品的贡献度 */
	public int getContributions() { return item.sellgold == 0 ? 1 : item.sellgold * 5; }
	
	/** 是否可以累加 */
	public boolean isCanCumsum() { return count < item.manymax; }
	
	/** 道具占用空间 */
	public int occupyRoom() { return item.usegrid; }
	
	/** 是否一个空的道具 */
	public boolean isEmpty() { return count <= 0; }
	
	/** 道具出售价格  */
	public int getSellgold() { return item.buygold; }

	/** 最大叠加数 */
	public int getMaxOverlap() { return item.manymax; }
	
	
	/** 是否货币 */
	public boolean isCurrency() { return item.itemtype == 0 && nid == LXConstants.CURRENCY_NID; }
	
	/** 是否舰船装备 */
	public boolean isShipEquip() { return type == PropType.EQUIP && item.itemtype != 4; }
	
	/** 是否舰长装备 */
	public boolean isCaptainEquip() { return type == PropType.EQUIP && item.itemtype == 4; }
	
	/** 是否舰长食用亲密豆 */
	public boolean isCaptainIntimacy() { return type == PropType.STUFF && item.itemtype == 5; }
	

	
	/**
	 * 添加数量 
	 * @param num
	 * @return 多出的 (如：最大叠加为5 3 + 3 = 6  返回1)
	 */
	public int addCount( int num ) {
		int ret 	= count + Math.abs( num );
		if( ret > item.manymax )
			count 	= item.manymax;
		else
			count	= ret;
		return ret > item.manymax ? ret - item.manymax : 0;
	}
	
	/**
	 * 扣除数量
	 * @param num
	 * @return 不够的 (如：3 - 5 = -2  返回2)
	 */
	public int deductCount( int num ){
		int ret 	= count - Math.abs( num );
		if( ret < 0 )
			count 	= 0;
		else
			count	= ret;
		return ret < 0 ? Math.abs(ret) : 0;
	}


}
