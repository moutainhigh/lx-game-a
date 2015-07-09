package cn.xgame.a.player.u;


import x.javaplus.mysql.db.Condition;

import cn.xgame.a.IFromDB;
import cn.xgame.gen.dto.MysqlGen.CaptainsDto;
import cn.xgame.gen.dto.MysqlGen.PropsDto;
import cn.xgame.gen.dto.MysqlGen.ShipsDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;

/**
 * 所有数据库 唯一ID 基础值
 * @author deng		
 * @date 2015-6-18 下午3:24:35
 */
public class DBBaseUID implements IFromDB{

	private Player player;
	
	private int PROP_UID 		= 0;
	private int SHIP_UID 		= 0;
	private int CAPTAIN_UID 	= 0;
	
	public DBBaseUID( Player player ) {
		this.player = player;
	}


	@Override
	public void fromDB() {
		String sql	= new Condition( PropsDto.unameChangeSql( player.getUID() ) ).AND( PropsDto.gsidChangeSql( player.getGsid() ) ).toString();
		PROP_UID 	= SqlUtil.getMaxId( SqlUtil.getClassName( PropsDto.class ), "uid", sql );
		
		sql			= new Condition( ShipsDto.unameChangeSql( player.getUID() ) ).AND( ShipsDto.gsidChangeSql( player.getGsid() ) ).toString();
		SHIP_UID	= SqlUtil.getMaxId( SqlUtil.getClassName( ShipsDto.class ), "uid", sql );
		
		sql			= new Condition( CaptainsDto.unameChangeSql( player.getUID() ) ).AND( CaptainsDto.gsidChangeSql( player.getGsid() ) ).toString();
		CAPTAIN_UID	= SqlUtil.getMaxId( SqlUtil.getClassName( CaptainsDto.class ), "uid", sql );
		
	}


	/**
	 * 生成一个 道具UID
	 * @return
	 */
	public int generatorPropUID(  ) {
		return ++PROP_UID;
	}
	
	/**
	 * 生成一个 舰船UID
	 * @return
	 */
	public int generatorShipUID(){
		return ++SHIP_UID;
	}
	
	/**
	 * 生成一个 舰长UID
	 * @return
	 */
	public int generatorCaptainUID(){
		return ++CAPTAIN_UID;
	}
	
	public static void main(String[] args) {
	}
	
}
