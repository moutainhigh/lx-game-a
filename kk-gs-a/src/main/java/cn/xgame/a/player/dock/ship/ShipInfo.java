package cn.xgame.a.player.dock.ship;


import java.util.List;

import x.javaplus.mysql.db.Condition;
import x.javaplus.util.ErrorCode;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.player.dock.ship.o.IHold;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.sequip.SEquipAttr;
import cn.xgame.a.prop.ship.ShipAttr;
import cn.xgame.gen.dto.MysqlGen.ShipsDao;
import cn.xgame.gen.dto.MysqlGen.ShipsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;

/**
 * 一个舰船 的信息
 * @author deng		
 * @date 2015-7-9 下午12:22:54
 */
public class ShipInfo implements ITransformStream{

	// 舰船属性
	private ShipAttr attr;
	
	// 当前血量
	private int currentHp				= 0;
	
	// 舰长唯一ID
	private int captainUID 				= -1;
	
	// 当前停靠星球ID -如果为-1那么就代表已经在舰队上面了 
	private int berthSid				= -1;
	
	// 货仓
	private IHold 	holds 			= new IHold();
	
	// 武器
	private IHold 	weapons 		= new IHold();
	
	// 辅助
	private IHold 	assists			= new IHold();
	
	
	/**
	 * 通过配置表创建一个
	 * @param uid
	 * @param nid
	 */
	public ShipInfo( int snid, int uid, int nid ) {
		attr = (ShipAttr) IProp.create( uid, nid, 1 );
		attr.randomAttachAttr();
		currentHp 	= attr.getMaxHp();
		berthSid	= snid;
		holds.setRoom( attr.getGroom() );
		weapons.setRoom( attr.getWroom() );
		assists.setRoom( attr.getEroom() );
	}

	/**
	 * 通过数据库获取
	 * @param dto
	 */
	public ShipInfo( ShipsDto dto ) {
		attr 		= (ShipAttr) IProp.create( dto.getUid(), dto.getNid(), 1 );
		attr.wrapAttachBytes( dto.getAttachAttr() );
		currentHp 	= dto.getCurrentHp();
		captainUID 	= dto.getCaptainUid();
		berthSid	= dto.getBerthSid();
		holds.fromBytes( dto.getHolds() );
		weapons.fromBytes( dto.getWeapons() );
		assists.fromBytes( dto.getAssists() );
	}
	
	//TODO---------数据库相关
	public void createDB( Player player ) {
		ShipsDao dao = SqlUtil.getShipsDao();
		ShipsDto dto = dao.create();
		dto.setGsid( player.getGsid() );
		dto.setUname( player.getUID() );
		dto.setUid( attr.getUid() );
		setDBData(dto);
		dao.commit(dto);
	}

	public void updateDB(Player player) {
		ShipsDao dao = SqlUtil.getShipsDao();
		String sql 	= new Condition( ShipsDto.gsidChangeSql( player.getGsid() ) ).
				AND( ShipsDto.unameChangeSql( player.getUID() ) ).AND( ShipsDto.uidChangeSql( attr.getUid() ) ).toString();
		ShipsDto dto = dao.updateByExact( sql );
		setDBData(dto);
		dao.commit(dto);
	}
	private void setDBData(ShipsDto dto) {
		dto.setNid( attr.getNid() );
		dto.setAttachAttr( attr.toAttachBytes() );
		dto.setCurrentHp( currentHp );
		dto.setCaptainUid( captainUID );
		dto.setBerthSid( berthSid );
		dto.setHolds( holds.toBytes() );
		dto.setWeapons( weapons.toBytes() );
		dto.setAssists( assists.toBytes() );
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( attr.getUid() );
		buffer.writeInt( attr.getNid() );
		attr.buildTransformStream(buffer);
		buffer.writeInt( currentHp );
		buffer.writeInt( captainUID );
		buffer.writeInt( berthSid );
		// 货仓
		holds.buildTransformStream(buffer);
		// 武器
		weapons.buildTransformStream(buffer);
		// 辅助
		assists.buildTransformStream(buffer);
	}
	
	public ShipAttr attr(){ return attr; }
	public int getuId() { return attr.getUid(); }
	public int getnId() { return attr.getNid(); }
	public IHold getHolds() { return holds; }
	public IHold getWeapons() { return weapons; }
	public IHold getAssists() { return assists; }

	public int getCurrentHp() { 
		return currentHp; 
	}
	public void setCurrentHp(int currentHp) { 
		this.currentHp = currentHp; 
	}
	public int getCaptainUID() { 
		return captainUID; 
	}
	public void setCaptainUID(int captainUID) { 
		this.captainUID = captainUID; 
	}
	public int getBerthSid() {
		return berthSid;
	}
	public void setBerthSid( int berthSid ) {
		this.berthSid = berthSid;
	}

	/**
	 * 是否拥有舰长
	 * @return
	 * @throws Exception
	 */
	public boolean isHaveCaptain() throws Exception {
		if( captainUID == -1 )
			throw new Exception( ErrorCode.OTHER_ERROR.name() );
		return true;
	}

	/**
	 * 获取一个装备
	 * @param puid
	 * @return
	 */
	public IProp getEquip( int puid ) {
		IProp ret = weapons.getProp(puid);
		return ret == null ? assists.getProp(puid) : ret;
	}

	/**
	 * 删除一个装备
	 * @param puid
	 * @return
	 */
	public IProp removeEquip( int puid ) {
		IProp ret = getEquip( puid );
		if( ret == null ) return null;
		if( !weapons.remove(ret) )
			assists.remove(ret);
		return ret;
	}
	
	/**
	 * 包装 攻击防御属性
	 * @param fighter
	 */
	public void wrapAttackattr( Fighter fighter ) {
		List<IProp> props = weapons.getAll();
		for( IProp prop : props ){
			SEquipAttr weapon = (SEquipAttr) prop;
			fighter.attacks.addAll( weapon.getAtks() );
			fighter.defends.addAll( weapon.getDefs() );
		}
	}
	
}
