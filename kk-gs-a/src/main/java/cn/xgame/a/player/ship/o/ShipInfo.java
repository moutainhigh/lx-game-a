package cn.xgame.a.player.ship.o;

import java.util.List;

import x.javaplus.mysql.db.Condition;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.award.AwardInfo;
import cn.xgame.a.chat.AxnControl;
import cn.xgame.a.chat.ChatManager;
import cn.xgame.a.chat.o.AxnInfo;
import cn.xgame.a.chat.o.IAxnCrew;
import cn.xgame.a.chat.o.v.TeamAxnCrew;
import cn.xgame.a.combat.o.Answers;
import cn.xgame.a.combat.o.Askings;
import cn.xgame.a.combat.o.AtkAndDef;
import cn.xgame.a.player.IUObject;
import cn.xgame.a.player.ship.o.bag.EquipControl;
import cn.xgame.a.player.ship.o.bag.HoldControl;
import cn.xgame.a.player.ship.o.v.EctypeCombatInfo;
import cn.xgame.a.player.ship.o.v.SailPurpose;
import cn.xgame.a.player.ship.o.v.ShipStatus;
import cn.xgame.a.player.ship.o.v.StatusControl;
import cn.xgame.a.player.ship.o.v.TempRecordInfo;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.ShipPo;
import cn.xgame.config.o.StarsPo;
import cn.xgame.gen.dto.MysqlGen.ShipsDao;
import cn.xgame.gen.dto.MysqlGen.ShipsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.utils.Logs;
import cn.xgame.utils.LuaUtil;

/**
 * 一个舰船 的信息
 * @author deng		
 * @date 2015-7-9 下午12:22:54
 */
public class ShipInfo extends IUObject implements ITransformStream{

	private final AxnControl chatControl = ChatManager.o.getChatControl();
	private final ShipPo template;
	
	
	// 舰长唯一ID
	private int captainUID = -1;
	
	// 货仓
	private HoldControl holds 			= new HoldControl();
	
	// 装备 武器-辅助
	private EquipControl equips 		= new EquipControl();
	
	// 状态
	private StatusControl status 		= new StatusControl();
	
	// 副本信息   (这里只有是战斗航行和战斗状态 才有)
	private EctypeCombatInfo keepInfo 	= new EctypeCombatInfo(); 
	
	// 组队 id
	private int teamId 					= 0;
	
	// 血量
	private int currentHp				= 0;
	
	// 临时记录信息  该数据不用保存数据库
	private TempRecordInfo temprecord 	= new TempRecordInfo();
	
	
	/**
	 * 通过配置表创建一个
	 * @param uid
	 * @param nid
	 */
	public ShipInfo( int uid, int nid ) {
		super( uid, nid );
		template = CsvGen.getShipPo(nid);
		currentHp = template.hp;
		holds.setRoom( template.groom );
		equips.setWroom( template.wroom );
		equips.setEroom( template.eroom );
	}

	/**
	 * 通过数据库获取
	 * @param dto
	 */
	public ShipInfo( ShipsDto dto ) {
		super( dto.getUid(), dto.getNid() );
		template = CsvGen.getShipPo( dto.getNid() );
		currentHp = dto.getCurrentHp();
		captainUID = dto.getCaptainUid();
		status.fromBytes( dto.getStatuss() );
		keepInfo.fromBytes( dto.getKeepinfos() );
		holds.fromBytes( dto.getHolds() );
		equips.fromBytes( dto.getEquips() );
		teamId = chatControl.getAXNInfo(dto.getTeamAxnid()) == null ? 0 : dto.getTeamAxnid();
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( getuId() );
		buffer.writeInt( getnId() );
		buffer.writeInt( captainUID );
		// 状态
		status.buildTransformStream(buffer);
		if( status.getStatus() == ShipStatus.SAILING ){
			buffer.writeInt( status.getSurplusTime() );
			buffer.writeInt( status.getTargetSnid() );
			buffer.writeByte( status.getSailPurpose().toNumber() );
			if( status.getSailPurpose() == SailPurpose.FIGHTING )
				buffer.writeInt( keepInfo.getEnid() );
		}
		if( status.getStatus() == ShipStatus.COMBAT ){
			buffer.writeInt( status.getSurplusTime() );
			buffer.writeInt( keepInfo.getEnid() );
			buffer.writeInt( keepInfo.isWin() );
		}
		// 货仓
		holds.buildTransformStream(buffer);
		// 装备
		equips.buildTransformStream(buffer);
		// 组队
		buffer.writeInt( teamId );
		AxnInfo axn = chatControl.getAXNInfo(teamId);
		buffer.writeByte( axn == null ? 0: axn.getAxnCrews().size() );
		if( axn != null ){
			for( IAxnCrew crew : axn.getAxnCrews() ){
				TeamAxnCrew team = (TeamAxnCrew)crew;
				team.buildTransformStream(buffer);
			}
		}
	}

	
	//TODO---------数据库相关
	public void createDB( Player player ) {
		ShipsDao dao = SqlUtil.getShipsDao();
		ShipsDto dto = dao.create();
		dto.setGsid( player.getGsid() );
		dto.setUname( player.getUID() );
		dto.setUid( getuId() );
		setDBData(dto);
		dao.commit(dto);
	}

	public void updateDB(Player player) {
		ShipsDao dao = SqlUtil.getShipsDao();
		String sql 	= new Condition( ShipsDto.gsidChangeSql( player.getGsid() ) ).
				AND( ShipsDto.unameChangeSql( player.getUID() ) ).AND( ShipsDto.uidChangeSql( getuId() ) ).toString();
		ShipsDto dto = dao.updateByExact( sql );
		setDBData(dto);
		dao.commit(dto);
	}
	private void setDBData(ShipsDto dto) {
		dto.setNid( getnId() );
		dto.setCurrentHp( currentHp );
		dto.setCaptainUid( captainUID );
		dto.setStatuss( status.toBytes() );
		dto.setKeepinfos( keepInfo.toBytes() );
		dto.setHolds( holds.toBytes() );
		dto.setEquips( equips.toBytes() );
		dto.setTeamAxnid( teamId );
	}
	

	public ShipPo template(){ return template; }
	public int getCaptainUID() { return captainUID; }
	public void setCaptainUID(int captainUID) { this.captainUID = captainUID; }
	public void setStatus( ShipStatus shipStatus ) { status.setStatus(shipStatus); }
	public int getTeamId() { return teamId; }
	public void setTeamId(int teamId) { this.teamId = teamId; }
	public int getCurrentHp() { return currentHp; }
	public void setCurrentHp(int curHp) { this.currentHp = curHp; }
	public HoldControl getHolds() { return holds; }
	public EquipControl getEquips() { return equips; }
	public StatusControl getStatus() { return status; }
	public EctypeCombatInfo getKeepInfo() { return keepInfo; }
	public TempRecordInfo getTemprecord() { return temprecord; }
	/**
	 * 是否悬停
	 * @return
	 */
	public boolean isLevitation() {
		return status.getStatus() == ShipStatus.LEVITATION;
	}
	
	/**
	 * 是否可以战斗
	 * @return
	 */
	public boolean isCanFighting() {
		return captainUID != -1;
	}
	
	/**
	 * 获取航行到目标星球 的航行时间
	 * @param snid 目标星球
	 * @return 需要的航行时间 
	 */
	public int getSailingTime( int snid ) {
		if( status.getCurrentSnid() == snid )
			return 0;
		StarsPo cur	 	= CsvGen.getStarsPo( status.getCurrentSnid() );
		StarsPo to 		= CsvGen.getStarsPo( snid );
		Lua lua 		= LuaUtil.getEctypeCombat();
		LuaValue[] ret 	= lua.getField( "sailingTime" ).call( 1, cur, to );
		int sailTime 	= ret[0].getInt();
		Logs.debug( "获取航行时间  " +status.getTargetSnid()+ " -> " +snid+ "  时间：" + sailTime );
		return sailTime;
	}
	
	/**
	 * 记录 副本战斗信息
	 * @param enid
	 * @param combatTime
	 * @param isWin
	 * @param awards
	 */
	public void recordEctypeCombatInfo(int enid, byte isWin, List<AwardInfo> awards) {
		keepInfo.setEnid( enid );
		keepInfo.setWin( isWin );
		if( awards != null )
			keepInfo.addAwards( awards );
	}
	
	/**
	 * 塞入战斗属性
	 * @param attacks
	 * @param defends
	 * @param askings
	 * @param answers
	 * @return 血量
	 */
	public int warpFightProperty(List<AtkAndDef> attacks, List<AtkAndDef> defends, 
			List<Askings> askings, List<Answers> answers) {
		// 血量
		int ret = template.hp;
		// 塞入装备的属性
		ret	+= equips.warpFightProperty(attacks, defends, askings, answers);
		return ret;
	}
	
	
}
