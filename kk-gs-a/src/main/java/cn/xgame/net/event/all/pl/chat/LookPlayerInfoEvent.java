package cn.xgame.net.event.all.pl.chat;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;

import x.javaplus.collections.Lists;
import x.javaplus.util.ErrorCode;

import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.WorldManager;
import cn.xgame.a.world.planet.home.HomePlanet;
import cn.xgame.a.world.planet.home.o.Child;
import cn.xgame.config.o.ReclaimPo;
import cn.xgame.net.event.IEvent;
import cn.xgame.net.netty.Netty.RW;

/**
 * 查看玩家信息
 * @author deng		
 * @date 2015-10-23 下午3:34:46
 */
public class LookPlayerInfoEvent extends IEvent{

	@Override
	public void run(Player player, ByteBuf data) throws IOException {
		
		String UID = RW.readString(data);
		
		ErrorCode code = null;
		Player temp = null;
		try {
			temp = PlayerManager.o.getPlayerFmOnline(UID);
			if( temp == null )
				throw new Exception( ErrorCode.PLAYER_NOTEXIST.name() );
			
			code = ErrorCode.SUCCEED;
		} catch (Exception e) {
			code = ErrorCode.valueOf( e.getMessage() );
		}
		
		ByteBuf buffer = buildEmptyPackage( player.getCtx(), 512 );
		buffer.writeShort( code.toNumber() );
		if( code == ErrorCode.SUCCEED ){
			RW.writeString( buffer, temp.getNickname() );
			RW.writeString( buffer, temp.getUID() );
			buffer.writeInt( temp.getHeadIco() );
			buffer.writeInt( temp.getCountryId() );
			buffer.writeByte( getHonor(temp) );
			buffer.writeByte( getCapNum(temp) );
			buffer.writeByte( getManorLevel(temp) );
			List<HomePlanet> ls = getPlanetList(temp);
			buffer.writeByte( ls.size() );
			for( HomePlanet home : ls ){
				Child child = home.getChild(temp.getUID());
				buffer.writeInt( home.getId() );
				buffer.writeShort( child.getPrivilege() );
			}
		}
		sendPackage( player.getCtx(), buffer );
	}
	
	// 获取所拥有的星球列表
	private List<HomePlanet> getPlanetList(Player temp){
		try {
			HomePlanet home = WorldManager.o.getHPlanetInPlayer(temp);
			return Lists.newArrayList(home);
		} catch (Exception e) {
			return Lists.newArrayList();
		}
	}
	
	// 获取领地等级
	private int getManorLevel(Player temp) {
		ReclaimPo territory = temp.getManors().getTerritory();
		return territory == null ? 0 : territory.level;
	}

	// 获取舰长数 
	private int getCapNum(Player temp) {
		return temp.getDocks().getCabin().size();
	}

	// 获取在母星的头衔
	private int getHonor(Player temp) {
		try {
			HomePlanet home = WorldManager.o.getHPlanetInPlayer(temp);
			return home.getHonor( temp );
		} catch (Exception e) {
			return 3;
		}
	}

}
