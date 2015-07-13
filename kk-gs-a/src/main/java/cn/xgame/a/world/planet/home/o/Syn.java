package cn.xgame.a.world.planet.home.o;

import java.util.List;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.prop.IProp;
import cn.xgame.a.world.planet.data.building.UnBuildings;
import cn.xgame.a.world.planet.data.specialty.Specialty;
import cn.xgame.a.world.planet.data.tech.UnTechs;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.pl.update.Update_2211;
import cn.xgame.net.event.all.pl.update.Update_2221;
import cn.xgame.net.event.all.pl.update.Update_2231;
import cn.xgame.net.event.all.pl.update.Update_2241;

/**
 * 同步消息类
 * @author deng		
 * @date 2015-7-11 下午12:04:28
 */
public class Syn {

	/**
	 * 特产同步
	 * @param childs
	 * @param spe
	 */
	public static void specialty(List<Child> childs, Specialty spe) {
		for( Child child : childs ){
			Player player = PlayerManager.o.getPlayerFmOnline( child.getUID() );
			if( player == null || !player.isOnline() )
				continue;
			
			((Update_2211)Events.UPDATE_2211.getEventInstance()).run( player, spe );
		}
	}
	
	/**
	 * 星球建筑更新包 - 投票中 && 建筑中 && 已建筑
	 * @param childs
	 * @param status 1.投票中 2.拒绝建筑 3.开始建筑 4.建筑成功
	 * @param voteBuild
	 */
	public static void build(List<Child> childs, int status, UnBuildings voteBuild) {
		for( Child child : childs ){
			Player player = PlayerManager.o.getPlayerFmOnline( child.getUID() );
			if( player == null || !player.isOnline() ) continue;
			((Update_2241)Events.UPDATE_2241.getEventInstance()).run( player, status, voteBuild );
		}
	}
	
	/**
	 * 更新 科技  - 投票中 && 研究中 && 已研究
	 * @param childs
	 * @param status 1.投票中 2.拒绝研究 3.开始研究 4.研究成功
	 * @param unTechs
	 */
	public static void tech(List<Child> childs, int status, UnTechs unTechs) {
		for( Child child : childs ){
			Player player = PlayerManager.o.getPlayerFmOnline( child.getUID() );
			if( player == null || !player.isOnline() ) continue;
			((Update_2231)Events.UPDATE_2231.getEventInstance()).run( player, status, unTechs );
		}	
	}
	
	/**
	 * 星球资源同步玩家
	 * @param childs
	 * @param update
	 */
	public static void res( List<Child> childs, List<IProp> update ) {
		for( Child child : childs ){
			Player player = PlayerManager.o.getPlayerFmOnline( child.getUID() );
			if( player == null || !player.isOnline() ) continue;
			((Update_2221)Events.UPDATE_2221.getEventInstance()).run( player, update );
		}
	}





}
