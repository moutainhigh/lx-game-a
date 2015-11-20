package cn.xgame.a.player.dock.ship;


import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.mysql.db.Condition;
import x.javaplus.util.ErrorCode;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.fighter.DamagedInfo;
import cn.xgame.a.fighter.Fighter;
import cn.xgame.a.fighter.o.Answers;
import cn.xgame.a.player.dock.classes.IHold;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.prop.info.EquipAuxiliaryAttr;
import cn.xgame.a.prop.info.EquipWeaponAttr;
import cn.xgame.a.prop.info.ShipAttr;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.AnswerPo;
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
	
	// 当前停靠星球ID
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
	public void deleteDB( Player player ){
		ShipsDao dao 	= SqlUtil.getShipsDao();
		String sql 		= new Condition( ShipsDto.uidChangeSql( attr.getUid() ) ).AND( ShipsDto.gsidChangeSql( player.getGsid() ) ).
				AND( ShipsDto.unameChangeSql( player.getUID() ) ).toString();
		dao.deleteByExact( sql );
		dao.commit();
	}
	
	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( attr.getNid() );
		buffer.writeInt( attr.getUid() );
		buffer.writeByte( attr.getQuality().toNumber() );
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
	public void addCurrentHp(int damaged) {
		this.currentHp += damaged;
		if( this.currentHp < 0 )
			this.currentHp = 0;
		if( this.currentHp > attr.getMaxHp() )
			this.currentHp = attr.getMaxHp();
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
			throw new Exception( ErrorCode.CAPTAIN_NOTEXIST.name() );
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
	 * 获取所有装备
	 * @return
	 */
	public List<IProp> getAllEquip() {
		List<IProp> ret = Lists.newArrayList();
		ret.addAll( weapons.getAll() );
		ret.addAll( assists.getAll() );
		return ret;
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
	 * 获取该船所有占用能量
	 * @return
	 */
	public int allOccupyEnergy() {
		int ret = 0;
		for( IProp o : weapons.getAll() )
			ret += ((EquipWeaponAttr)o).getEnergy();
		for( IProp o : assists.getAll() )
			ret += ((EquipAuxiliaryAttr)o).getEnergy();
		return ret;
	}
	
	/**
	 * 获取所有装备的复杂度
	 * @return
	 */
	public int allEctypeComplexity(){
		int ret = 0;
		for( IProp o : weapons.getAll() )
			ret += ((EquipWeaponAttr)o).getPerplexity();
		for( IProp o : assists.getAll() )
			ret += ((EquipAuxiliaryAttr)o).getPerplexity();
		return ret;
	}
	
	/**
	 * 获取所有装备精密度
	 * @return
	 */
	public int getAllaccuracy() {
		int ret = 0;
		for( IProp o : weapons.getAll() )
			ret += ((EquipWeaponAttr)o).getAccuracy();
		for( IProp o : assists.getAll() )
			ret += ((EquipAuxiliaryAttr)o).getAccuracy();
		return ret;
	}
	
	/** 装满弹药  */
	public void fillupAmmo( ) {
		for( IProp o : weapons.getAll() ){
			EquipWeaponAttr weapon = (EquipWeaponAttr) o;
			weapon.fillupAmmo();
		}
	}
	
	/**
	 * 减少弹药
	 */
	public void toreduceAmmo( int value ){
		for( IProp o : weapons.getAll() ){
			EquipWeaponAttr weapon = (EquipWeaponAttr) o;
			int cur = weapon.getCurAmmo() + value;
			weapon.setCurAmmo(cur);
		}
	}
	
	/**
	 * 包装 攻击防御属性
	 * @param fighter
	 */
	public void wrapAttackattr( Fighter fighter ) {
		// 舰船本身的应答
		if( !attr.templet().answer.isEmpty() ){
			String[] content = attr.templet().answer.split( ";" );
			for( String id : content ){
				AnswerPo answer = CsvGen.getAnswerPo( Integer.parseInt(id) );
				fighter.answer.add( new Answers(answer) );
			}
		}
		// 装备应答和攻击属性
		List<IProp> props = weapons.getAll();
		for( IProp prop : props ){
			EquipWeaponAttr weapon = (EquipWeaponAttr) prop;
			if( weapon.itemType() == 1 )
				fighter.addAtkattr( weapon.getBattleAttrs());
			if( weapon.itemType() == 2 )
				fighter.addDefattr( weapon.getBattleAttrs() );
			for( int id : weapon.getAnswers() ){
				AnswerPo answer = CsvGen.getAnswerPo( id );
				fighter.answer.add( new Answers(answer) );
			}
		}
	}

	/**
	 * 计算战损
	 * @param ret
	 * @param scale
	 */
	public void computeEquipDamage( DamagedInfo ret, float scale ) {
		// 武器
		List<IProp> removes = Lists.newArrayList();
		for( IProp o : weapons.getAll() ){
			EquipWeaponAttr equip = (EquipWeaponAttr) o;
			equip.addCurrentDur( - (int) (equip.getAccuracy() * scale) );
			if( equip.getCurrentDur() < equip.getMaxDur() ){
				ret.addLossEquip( this, equip.getUid(), equip.getCurrentDur() );
				if( equip.getCurrentDur() == 0 )
					removes.add(o);
			}
		}
		weapons.getAll().removeAll(removes);
		// 辅助
		removes.clear();
		for( IProp o : assists.getAll() ){
			EquipWeaponAttr equip = (EquipWeaponAttr) o;
			equip.addCurrentDur( - (int) (equip.getAccuracy() * scale) );
			if( equip.getCurrentDur() < equip.getMaxDur() ){
				ret.addLossEquip( this, equip.getUid(), equip.getCurrentDur() );
				if( equip.getCurrentDur() == 0 )
					removes.add(o);
			}
		}
		assists.getAll().removeAll(removes);
	}
	
}
