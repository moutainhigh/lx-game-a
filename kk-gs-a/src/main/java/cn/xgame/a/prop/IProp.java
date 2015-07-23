package cn.xgame.a.prop;

import x.javaplus.mysql.db.Condition;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Item;
import cn.xgame.gen.dto.MysqlGen.PropsDao;
import cn.xgame.gen.dto.MysqlGen.PropsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import io.netty.buffer.ByteBuf;

/**
 * 道具 基类
 * @author deng		
 * @date 2015-6-17 下午7:02:11
 */
public abstract class IProp implements ITransformStream{
	
	// 基础物品表
	private final Item item;
	
	// 唯一ID
	private int uId;
	// 表格ID
	private int nId;
	// 数量
	private int count;
	
	/**
	 * 创建一个 并保存到数据库
	 * @param uid
	 * @param nid
	 * @param count
	 */
	public IProp( int uid, int nid, int count ){
		this.uId 	= uid;
		this.nId 	= nid;
		this.item 	= CsvGen.getItem(nid);
		addCount( count );
	}
	
	/**
	 * 从数据库获取数据
	 * @param o
	 */
	public IProp( PropsDto o ){
		this.uId 	= o.getUid();
		this.nId 	= o.getNid();
		this.item 	= CsvGen.getItem(nId);
		addCount( o.getCount() );
	}
	
	/**
	 * 写入基础数据到buffer
	 * @param buffer
	 */
	public void putBaseBuffer( ByteBuf buffer ) {
		buffer.writeInt(uId);
		buffer.writeInt(nId);
		buffer.writeInt(count);
	}
	
	/**
	 * 克隆一个
	 */
	public abstract IProp clone();
	
	/**
	 * 道具类型
	 * @return
	 */
	public abstract PropType type();
	
	/**
	 * 把附加属性塞入
	 * @param buf
	 */
	public abstract void putAttachBuffer( ByteBuf buf );

	/**
	 * 获取 附加属性
	 * @param buf
	 */
	public abstract void wrapAttach( ByteBuf buf ) ;
	
	/**
	 * 在数据库创建数据
	 * @param player
	 */
	public abstract void createDB( Player player );
	protected void create( Player player, byte[] attach ){
		PropsDao dao = SqlUtil.getPropsDao();
		PropsDto dto = dao.create();
		dto.setGsid( player.getGsid() );
		dto.setUname( player.getUID() );
		dto.setUid( getuId() );
		dto.setNid( getnId() );
		dto.setCount( getCount() );
		dto.setAttach( attach );
		dao.commit(dto);
	}
	
	/**
	 * 更新数据库数据
	 * @param player
	 */
	public abstract void updateDB( Player player );
	protected void update( Player player, byte[] attach ) {
		PropsDao dao 	= SqlUtil.getPropsDao();
		String sql 		= new Condition( PropsDto.uidChangeSql( getuId() ) ).AND( PropsDto.gsidChangeSql( player.getGsid() ) ).
				AND( PropsDto.unameChangeSql( player.getUID() ) ).toString();
		PropsDto dto	= dao.updateByExact( sql );
		dto.setNid( getnId() );
		dto.setCount( getCount() );
		dto.setAttach( attach );
		dao.commit(dto);
	}
	
	/**
	 * 从数据库删除数据
	 * @param player
	 */
	public void deleteDB( Player player ){
		PropsDao dao 	= SqlUtil.getPropsDao();
		String sql 		= new Condition( PropsDto.uidChangeSql( getuId() ) ).AND( PropsDto.gsidChangeSql( player.getGsid() ) ).
				AND( PropsDto.unameChangeSql( player.getUID() ) ).toString();
		dao.deleteByExact(sql);
		dao.commit();
	}
	
	public Item item(){ return item; }
	public int getuId() { return uId; }
	public void setuId(int uId) { this.uId = uId; }
	public int getnId() { return nId; }
	public void setnId(int nId) { this.nId = nId; }
	public int getCount() { return count; }
	public void setCount(int count) { this.count = count; }
	
	public String toString(){
		return type().name() + ", uId=" + uId + ", nId=" + nId + ", count=" + count; 
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
	public short occupyRoom() {
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
	 * 添加数量 
	 * @param num
	 * @return 多出的
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
	 * @return 不够的
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
