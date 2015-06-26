package cn.xgame.a.world.planet.home;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;


import cn.xgame.a.player.PlayerManager;
import cn.xgame.a.player.u.Player;
import cn.xgame.a.world.planet.IPlanet;
import cn.xgame.a.world.planet.data.tech.TechControl;
import cn.xgame.config.o.Stars;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDao;
import cn.xgame.gen.dto.MysqlGen.PlanetDataDto;
import cn.xgame.gen.dto.MysqlGen.SqlUtil;
import cn.xgame.net.netty.Netty.RW;

/**
 * 一个母星
 * @author deng		
 * @date 2015-6-18 下午4:24:05
 */
public class HomePlanet extends IPlanet {

	public HomePlanet(Stars clone) {
		super(clone);
	}


	// 玩家列表
	private Map<String, Player> players = new HashMap<String, Player>();
	
	// 科技列表
	private TechControl techs = new TechControl();

	@Override
	public void wrap( PlanetDataDto dto ) {
		super.wrap(dto);
		wrapPlayer( dto.getPlayers() );
		techs.fromBytes( dto.getTechs() );
	}

	private void wrapPlayer( byte[] data ) {
		if( data == null ) return ;
		ByteBuf buf = Unpooled.copiedBuffer(data);
		short size = buf.readShort();
		for( int i = 0; i < size; i++ ){
			String uid = RW.readString(buf);
			short gsid = buf.readShort();
			Player player = PlayerManager.o.getPlayer( uid, gsid );
			players.put(uid, player);
		}
	}
	
	private byte[] toPlayers(){
		ByteBuf buf = Unpooled.buffer( 1024 );
		buf.writeShort( players.size() );
		for( Player player : players.values() ){
			RW.writeString( buf, player.getUID() );
			buf.writeShort( player.getGsid() );
		}
		return buf.array();
	}
	

	/**
	 * 星球结构
	 * id ：Short
	 * 
	 * 
	 */
	@Override
	public void buildTransformStream( ByteBuf buffer ) {
		super.buildTransformStream(buffer);
		techs.buildTransformStream(buffer);
	}


	/**
	 * 添加一个 玩家
	 * @param player
	 */
	public void add( Player player ) {
		
		if( players.get(player.getUID()) == null )
			players.put( player.getUID(), player );
		
		updateDB();
	}

	// 更新数据库
	private void updateDB() {
		PlanetDataDao dao = SqlUtil.getPlanetDataDao();
		PlanetDataDto dto = dao.update();
		dto.setId( getId() );
		dto.setMaxSpace( getMaxSpace() );
		dto.setBuildings( getBuildings().toBytes() );
		dto.setDepots( getDepots().toBytes() );
		dto.setSpecialtys( getSpecialtys().toBytes() );
		dto.setTechs( techs.toBytes() );
		dto.setPlayers( toPlayers() );
		dao.commit(dto);
	}
	
}
