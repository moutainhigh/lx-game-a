package cn.xgame.a.player.ship.o;

import x.javaplus.mysql.db.Condition;
import x.javaplus.util.lua.Lua;
import x.javaplus.util.lua.LuaValue;
import io.netty.buffer.ByteBuf;
import cn.xgame.a.ITransformStream;
import cn.xgame.a.player.IUObject;
import cn.xgame.a.player.ship.o.v.HoldControl;
import cn.xgame.a.player.ship.o.v.EquipControl;
import cn.xgame.a.player.u.Player;
import cn.xgame.config.gen.CsvGen;
import cn.xgame.config.o.Ship;
import cn.xgame.config.o.Stars;
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

	private final Ship template;
	
	
	// 舰长唯一ID
	private int captainUID = -1;
	// 停靠在的星球
	private int starId;
	
	
	// 货仓
	private HoldControl holds = new HoldControl();
	
	// 装备 武器-辅助
	private EquipControl equips = new EquipControl();

	
	// 组队信息
	// TODO
	
	/**
	 * 通过配置表创建一个
	 * @param uid
	 * @param nid
	 */
	public ShipInfo( int uid, int nid ) {
		super( uid, nid );
		template 	= CsvGen.getShip(nid);
		holds.setRoom( template.groom );
		equips.setRoom( template.wroom );
	}

	/**
	 * 通过数据库获取
	 * @param dto
	 */
	public ShipInfo( ShipsDto dto ) {
		super( dto.getUid(), dto.getNid() );
		template = CsvGen.getShip( dto.getNid() );
		captainUID = dto.getCaptainUid();
		starId = dto.getStarId();
		holds.fromBytes( dto.getHolds() );
		equips.fromBytes( dto.getEquips() );
	}

	@Override
	public void buildTransformStream(ByteBuf buffer) {
		buffer.writeInt( getuId() );
		buffer.writeInt( getnId() );
	}

	
	
	/**
	 * 获取到某个星球的航行时间 单位秒
	 * @param sId
	 * @return
	 */
	public int getSailingTime( int sId ) {
		Stars cur = CsvGen.getStars(starId);
		Stars to = CsvGen.getStars(sId);
		Lua lua = LuaUtil.getGameData();
		LuaValue[] ret = lua.getField( "sailingTime" ).call( 1, cur, to, this );
		Logs.debug( "航行时间：" + ret[0].getInt() );
		return ret[0].getInt();
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
		dto.setCaptainUid( captainUID );
		dto.setStarId( starId );
		dto.setHolds( holds.toBytes() );
		dto.setEquips( equips.toBytes() );
	}
	
	
	public Ship template(){ return template; }
	public int getCaptainUID() {
		return captainUID;
	}
	public void setCaptainUID(int captainUID) {
		this.captainUID = captainUID;
	}
	public int getStarId() {
		return starId;
	}
	public void setStarId(int starId) {
		this.starId = starId;
	}
	public HoldControl getHolds() { return holds; }
	public EquipControl getEquips() { return equips; }
	
	
}
