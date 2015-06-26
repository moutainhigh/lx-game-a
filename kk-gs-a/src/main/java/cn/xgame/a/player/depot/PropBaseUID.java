package cn.xgame.a.player.depot;

import java.sql.SQLException;

import x.javaplus.mysql.db.Condition;

import cn.xgame.a.IFromDB;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.PropType;
import cn.xgame.gen.dto.MysqlGen.M_captainDto;
import cn.xgame.gen.dto.MysqlGen.M_cequipDto;
import cn.xgame.gen.dto.MysqlGen.M_sequipDto;
import cn.xgame.gen.dto.MysqlGen.M_shipDto;
import cn.xgame.gen.dto.MysqlGen.M_stuffDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.utils.Logs;

/**
 * 所有道具唯一ID基础值
 * @author deng		
 * @date 2015-6-18 下午3:24:35
 */
public class PropBaseUID implements IFromDB{

	private Player player;
	
	private int STUFF_UID 	= 0;
	private int CAPTAIN_UID = 0;
	private int SHIP_UID 	= 0;
	private int CEQUIP_UID 	= 0;
	private int SEQUIP_UID 	= 0;
	
	public PropBaseUID( Player player ) {
		this.player = player;
	}


	@Override
	public void fromDB() {
		
		String sql;
		try {
			sql			= new Condition( M_stuffDto.unameChangeSql( player.getUID() ) ).AND( M_stuffDto.gsidChangeSql(player.getGsid() ) ).toString();
			STUFF_UID 	= SqlUtil.getMaxId( SqlUtil.getClassName( M_stuffDto.class ), "uid", sql );
			
			sql			= new Condition( M_captainDto.unameChangeSql( player.getUID() ) ).AND( M_captainDto.gsidChangeSql(player.getGsid() ) ).toString();
			CAPTAIN_UID	= SqlUtil.getMaxId( SqlUtil.getClassName( M_captainDto.class ), "uid", sql );
			
			sql			= new Condition( M_shipDto.unameChangeSql( player.getUID() ) ).AND( M_shipDto.gsidChangeSql(player.getGsid() ) ).toString();
			SHIP_UID	= SqlUtil.getMaxId( SqlUtil.getClassName( M_shipDto.class ), "uid", sql );
			
			sql			= new Condition( M_cequipDto.unameChangeSql( player.getUID() ) ).AND( M_cequipDto.gsidChangeSql(player.getGsid() ) ).toString();
			CEQUIP_UID	= SqlUtil.getMaxId( SqlUtil.getClassName( M_cequipDto.class ), "uid", sql );
			
			sql			= new Condition( M_sequipDto.unameChangeSql( player.getUID() ) ).AND( M_sequipDto.gsidChangeSql(player.getGsid() ) ).toString();
			SEQUIP_UID	= SqlUtil.getMaxId( SqlUtil.getClassName( M_sequipDto.class ), "uid", sql );
			
		} catch (SQLException e) {
			Logs.error( "PropBaseUID.fromDB", e );
		}
	}


	/**
	 * 生成一个UID
	 * @param type
	 * @return
	 */
	public int generatorUID( PropType type ) {
		switch( type ){
		case STUFF:
			return ++STUFF_UID;
		case CAPTAIN:
			return ++CAPTAIN_UID;
		case SHIP:
			return ++SHIP_UID;
		case CEQUIP:
			return ++CEQUIP_UID;
		case SEQUIP:
			return ++SEQUIP_UID;
		}
		return 1;
	}
	

	
	public static void main(String[] args) {
	}
	
}
