package cn.xgame.a.world.planet.home.o;

import java.util.List;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.data.building.UnBuildings;
import cn.xgame.a.world.planet.data.specialty.Specialty;
import cn.xgame.net.event.Events;
import cn.xgame.net.event.all.pl.update.Update_2211;
import cn.xgame.net.event.all.pl.update.Update_2242;

/**
 * 同步消息类
 * @author deng		
 * @date 2015-7-11 下午12:04:28
 */
public class Syn {

	public static void specialty(List<Child> childs, Specialty spe) {
		for( Child child : childs ){
			Player player = PlayerManager.o.getPlayerFmOnline( child.getUID() );
			if( player == null || !player.isOnline() )
				continue;
			
			((Update_2211)Events.UPDATE_2211.getEventInstance()).run( player, spe );
		}
	}
	
	/**
	 * 建筑投票列表 同步玩家
	 * @param isAdd
	 * @param voteBuild
	 */
	public static void buiVote( List<Child> childs, int isAdd, UnBuildings voteBuild ) {
		for( Child child : childs ){
			Player player = PlayerManager.o.getPlayerFmOnline( child.getUID() );
			if( player == null || !player.isOnline() ) continue;
			((Update_2242)Events.UPDATE_2242.getEventInstance()).run( player, isAdd, voteBuild );
		}
	}



}
