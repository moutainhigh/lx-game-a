package cn.xgame.a.prop;

import x.javaplus.mysql.db.Condition;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ItemPo;
import cn.xgame.gen.dto.MysqlGen.PropsDao;
import cn.xgame.gen.dto.MysqlGen.PropsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.net.netty.Netty.RW;
import cn.xgame.system.LXConstants;
import io.netty.buffer.ByteBuf;

/**
 * 道具 基类
 * @author deng		
 * @date 2015-6-17 下午7:02:11
 */
public abstract class IProp{
	
	// 基础物品表
	private final ItemPo item;
	private final PropType type;
	
	// 唯一ID
	private int uid;
	// 表格ID
	private int nid;
	// 数量
	private int count;
	
	
	/**
	 * 创建一个 并保存到数据库
	 * @param item 
	 * @param uid
	 * @param nid
	 * @param count
	 */
	public IProp( ItemPo item, int uid, int nid, int count ){
		this.uid 	= uid;
		this.nid 	= nid;
		this.item 	= item;
		this.type 	= PropType.fromNumber( item.bagtype );
		addCount( count );
	}
	
	/**
	 * 从buffer中获取数据
	 * @param o
	 */
	public IProp( ByteBuf buf ){
		this.uid 	= buf.readInt();
		this.nid 	= buf.readInt();
		addCount( buf.readInt() );
		wrapAttachBytes( RW.readBytes(buf) );
		this.item 	= CsvGen.getItemPo(nid);
		this.type 	= PropType.fromNumber( item.bagtype );
	}
	
	/**
	 * 拷贝一份
	 * @param clone
	 */
	public IProp( IProp clone ) {
		this.uid 	= clone.uid;
		this.nid 	= clone.nid;
		this.count 	= clone.count;
		this.item 	= clone.item;
		this.type 	= clone.type;
	}

	/**
	 * 创建一个简单的道具
	 * @param uid
	 * @param nid
	 * @param count
	 * @return
	 */
	public static IProp create( int uid, int nid, int count ) {
		ItemPo item 	= CsvGen.getItemPo(nid);
		if( item == null ) return null;
		PropType type 	= PropType.fromNumber( item.bagtype );
		return type.create( item, uid, nid, count );
	}
	
	/**
	 * 从数据库中创建一个道具出来
	 * @param buf
	 * @return
	 */
	public static IProp create( PropsDto o ) {
		IProp prop 	= create( o.getUid(), o.getNid(), o.getCount() );
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
		IProp prop 	= create( uid, nid, count );
		prop.wrapAttachBytes( RW.readBytes(buf) );
		return prop;
	}
	
	public String toString(){
		return type().name() + ", uId=" + uid + ", nId=" + nid + ", count=" + count; 
	}
	
	/**
	 * 写入基础数据到buffer
	 * @param buffer
	 */
	public void putBaseBuffer( ByteBuf buffer ) {
		buffer.writeInt(uid);
		buffer.writeInt(nid);
		buffer.writeInt(count);
	}
	
	/**
	 * 写入全部数据到buffer
	 * @param buf
	 */
	public void putBuffer( ByteBuf buffer ) {
		buffer.writeInt(uid);
		buffer.writeInt(nid);
		buffer.writeInt(count);
		buffer.writeBytes( toAttachBytes() );
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
	 * 在数据库创建数据
	 * @param player
	 */
	public void createDB( Player player ){
		PropsDao dao = SqlUtil.getPropsDao();
		PropsDto dto = dao.create();
		dto.setGsid( player.getGsid() );
		dto.setUname( player.getUID() );
		dto.setUid( uid );
		dto.setNid( nid );
		dto.setCount( count );
		dto.setAttach( toAttachBytes() );
		dao.commit(dto);
	}
	
	/**
	 * 更新数据库数据
	 * @param player
	 */
	public void updateDB( Player player ) {
		PropsDao dao 	= SqlUtil.getPropsDao();
		String sql 		= new Condition( PropsDto.uidChangeSql( uid ) ).AND( PropsDto.gsidChangeSql( player.getGsid() ) ).
				AND( PropsDto.unameChangeSql( player.getUID() ) ).toString();
		PropsDto dto	= dao.updateByExact( sql );
		dto.setNid( nid );
		dto.setCount( getCount() );
		dto.setAttach( toAttachBytes() );
		dao.commit(dto);
	}

	/**
	 * 从数据库删除数据
	 * @param player
	 */
	public void deleteDB( Player player ){
		PropsDao dao 	= SqlUtil.getPropsDao();
		String sql 		= new Condition( PropsDto.uidChangeSql( uid ) ).AND( PropsDto.gsidChangeSql( player.getGsid() ) ).
				AND( PropsDto.unameChangeSql( player.getUID() ) ).toString();
		dao.deleteByExact(sql);
		dao.commit();
	}
	
	public ItemPo item(){ return item; }
	public int getUid() { return uid; }
	public void setUid(int uId) { this.uid = uId; }
	public int getNid() { return nid; }
	public void setNid(int nId) { this.nid = nId; }
	public int getCount() { return count; }
	public void setCount(int count) { this.count = count; }
	
	/**
	 * 道具类型
	 * @return
	 */
	public PropType type(){ return type; }
	/**
	 * 道具小类型
	 * type == 2 舰船
	 * 1.
	 * 
	 * ------type == 3 舰船装备
	 * 1.武器(攻击)
	 * 2.防具(防御)
	 * 3.推进器
	 * 4.辅助
	 * 
	 * 
	 * @return
	 */
	public int itemType() {
		return item.itemtype;
	}
	
	/** 获取这个物品的贡献度 */
	public int getContributions() {
		return item.sellgold == 0 ? 1 : item.sellgold * 5;
	}
	
	/**
	 * 是否可以累加
	 * @return
	 */
	public boolean isCanCumsum() {
		return count < item.manymax;
	}
	
	/**
	 * 道具占用空间
	 * @return
	 */
	public int occupyRoom() {
		return item.usegrid;
	}
	
	/**
	 * 是否一个空的道具
	 * @return
	 */
	public boolean isEmpty() {
		return count <= 0;
	}
	
	/**
	 * 是否货币
	 * @return
	 */
	public boolean isCurrency() {
		return item.itemtype == 0 && nid == LXConstants.CURRENCY_NID;
	}
	
	/**
	 * 是否舰船装备
	 * @return
	 */
	public boolean isShipEquip() {
		return type() == PropType.SEQUIP;
	}
	
	/**
	 * 是否舰长装备
	 * @return
	 */
	public boolean isCaptainEquip() {
		return type() == PropType.CEQUIP;
	}
	
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

	/**
	 * 道具出售价格
	 * @return
	 */
	public int getSellgold() {
		return item.buygold;
	}

	/**
	 * 最大叠加数
	 * @return
	 */
	public int getMaxOverlap() {
		return item.manymax;
	}



}
