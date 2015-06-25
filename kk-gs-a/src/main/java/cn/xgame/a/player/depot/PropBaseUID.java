package cn.xgame.a.player.depot;

import java.sql.SQLException;

import x.javaplus.mysql.db.Condition;

import cn.xgame.a.IFromDB;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.PropType;
import cn.xgame.gen.dto.MysqlGen.CaptainDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.gen.dto.MysqlGen.StuffDto;
import cn.xgame.utils.Logs;

/**
 * 所有道具唯一ID基础值
 * @author deng		
 * @date 2015-6-18 下午3:24:35
 */
public class PropBaseUID implements IFromDB{

	private Player player;
	
	private int STUFF_UID;
	private int CAPTAIN_UID;
	private int SHIP_UID;
	private int CEQUIP_UID;
	private int SEQUIP_UID;
	
	public PropBaseUID( Player player ) {
		this.player = player;
	}


	@Override
	public void fromDB() {
		
		String sql;
		try {
			sql			= new Condition( StuffDto.unameChangeSql( player.getUID() ) ).AND( StuffDto.gsidChangeSql(player.getGsid() ) ).toString();
			STUFF_UID 	= SqlUtil.getMaxId( SqlUtil.getClassName( StuffDto.class ), "uid", sql );
			
			sql			= new Condition( CaptainDto.unameChangeSql( player.getUID() ) ).AND( CaptainDto.gsidChangeSql(player.getGsid() ) ).toString();
			CAPTAIN_UID	= SqlUtil.getMaxId( SqlUtil.getClassName( CaptainDto.class ), "uid", sql );
			
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
